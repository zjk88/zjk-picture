package com.zjk.zjkpicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjk.zjkpicturebackend.model.dto.user.UserQueryRequest;
import com.zjk.zjkpicturebackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjk.zjkpicturebackend.model.vo.LoginUserVO;
import com.zjk.zjkpicturebackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 27841
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2026-10-09 16:13:41
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 获取加密后的密码
     * @param userPassword
     * @return
     */
    String getEncryptPassword(String userPassword);

    /**
     *获取脱敏后的登录用户信息,把user转换为loginuserVO
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获得脱敏后的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获得脱敏后的用户信息列表
     *
     * @param userList
     * @return 脱敏后的用户列表
     */
    List<UserVO> getUserVOList(List<User> userList);


    /**
     * 获取当前的登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return 已登录返回 User，未登录返回 null（不抛异常）
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * "前端查询条件"到"数据库 WHERE 子句"的翻译器
     * 把前端的json语句转换为mybatis能识别的sql语句
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 是否为管理员
     *
     * @param user 用户对象
     * @return 是否为管理员（userRole 为 admin 时返回 true）
     */
    boolean isAdmin(User user);

}
