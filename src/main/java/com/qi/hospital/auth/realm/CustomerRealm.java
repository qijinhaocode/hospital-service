package com.qi.hospital.auth.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 自定义Realm
 */
public class CustomerRealm extends AuthorizingRealm {
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("==================");
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //在token中获取 用户名
        String principal = (String) token.getPrincipal();
        System.out.println(principal);

        //实际开发中应当 根据身份信息使用jdbc mybatis查询相关数据库
        //在这里只做简单的演示
        //假设username,password是从数据库获得的信息
        String username="zhangsan";
        String password="123456";
        if(username.equals(principal)){
            //参数1:返回数据库中正确的用户名
            //参数2:返回数据库中正确密码
            //参数3:提供当前realm的名字 this.getName();
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(principal,password,this.getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }
}