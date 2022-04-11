package com.qi.hospital.service;


import com.qi.hospital.dto.advice.AdviceRequest;
import com.qi.hospital.dto.advice.AdviceResponse;
import com.qi.hospital.dto.user.UserResponse;
import com.qi.hospital.mapper.AdviceMapper;
import com.qi.hospital.mapper.UserMapper;
import com.qi.hospital.model.advice.Advice;
import com.qi.hospital.model.user.User;
import com.qi.hospital.repository.AdviceRepository;
import com.qi.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdviceService {
    private final AdviceRepository adviceRepository;
    private final AdviceMapper adviceMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    public AdviceResponse createAdvice(String token, AdviceRequest adviceRequest) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(token);
        userService.validateUserExist(userOptional);

        String userId = userOptional.get().getId();
        adviceRequest.setUserId(userId);

        Advice advice = adviceMapper.toDomain(adviceRequest);
        Advice saveAdvice = adviceRepository.save(advice);

        UserResponse userResponse = userMapper.toResponse(userOptional.get());

        return AdviceResponse.builder()
                .advice(saveAdvice.getAdvice())
                .createTime(saveAdvice.getCreateDateTime())
                .userResponse(userResponse)
                .build();
    }

    // 根据反馈日期倒叙查所有意见
    public List<AdviceResponse> getAllAdvices() {
        List<Advice> allByOrderBOrderByCreateDateTimeDesc = adviceRepository.findAllByOrderByCreateDateTimeDesc();

        return allByOrderBOrderByCreateDateTimeDesc.stream().map(advice -> AdviceResponse.builder()
                .advice(advice.getAdvice())
                .createTime(advice.getCreateDateTime())
                .userResponse(userMapper.toResponse(userRepository.findById(advice.getUserId()).get()))
                .build()).collect(Collectors.toList());
    }
}
