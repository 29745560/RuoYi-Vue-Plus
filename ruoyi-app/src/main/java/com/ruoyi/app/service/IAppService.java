package com.ruoyi.app.service;

import com.ruoyi.app.domain.vo.AppGuestVo;
import com.ruoyi.app.domain.vo.AppRoomVo;
import com.ruoyi.app.domain.vo.AppUserVo;

public interface IAppService {

    /**
     * 获取用户信息
     *
     * @param userId 用户编号
     * @return 用户信息
     */
    public AppUserVo getUserInfo(Long userId);

    /**
     * 获取旅客入住信息
     *
     * @param roomId 房间编号
     * @param userId 用户编号
     * @return 旅客信息
     */
    public AppGuestVo getGuestInfo(Long roomId, Long userId);

    /**
     * 确认绑定身份证号
     *
     * @param roomId 房间编号
     * @param userId 用户编号
     * @return 结果
     */
    public Boolean bindingUserByCard(Long roomId, Long userId);

    /**
     * 获取房间信息
     *
     * @param roomId 房间编号
     * @param userId 用户编号
     * @return 房间信息
     */
    public AppRoomVo getRoomInfo(Long roomId, Long userId);
}
