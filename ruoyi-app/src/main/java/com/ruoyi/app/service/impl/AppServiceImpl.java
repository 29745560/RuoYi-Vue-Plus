package com.ruoyi.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.app.domain.AppCategory;
import com.ruoyi.app.domain.AppGoods;
import com.ruoyi.app.domain.AppGuest;
import com.ruoyi.app.domain.AppGuestScore;
import com.ruoyi.app.domain.AppRoom;
import com.ruoyi.app.domain.AppRoomRecord;
import com.ruoyi.app.domain.AppUser;
import com.ruoyi.app.domain.vo.AppGoodsVo;
import com.ruoyi.app.domain.vo.AppGuestVo;
import com.ruoyi.app.domain.vo.AppRoomVo;
import com.ruoyi.app.domain.vo.AppUserScoreVo;
import com.ruoyi.app.domain.vo.AppUserVo;
import com.ruoyi.app.mapper.AppCategoryMapper;
import com.ruoyi.app.mapper.AppGoodsMapper;
import com.ruoyi.app.mapper.AppGuestMapper;
import com.ruoyi.app.mapper.AppGuestScoreMapper;
import com.ruoyi.app.mapper.AppRoomMapper;
import com.ruoyi.app.mapper.AppRoomRecordMapper;
import com.ruoyi.app.mapper.AppUserMapper;
import com.ruoyi.app.service.IAppService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户登录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AppServiceImpl implements IAppService {

    private final AppCategoryMapper appCategoryMapper;
    private final AppGoodsMapper appGoodsMapper;
    private final AppGuestMapper appGuestMapper;
    private final AppGuestScoreMapper appGuestScoreMapper;
    private final AppRoomMapper appRoomMapper;
    private final AppRoomRecordMapper appRoomRecordMapper;
    private final AppUserMapper appUserMapper;

    /**
     * 获取用户信息
     *
     * @param userId 用户编号
     * @return 用户信息
     */
    @Override
    public AppUserVo getUserInfo(Long userId) {
        AppUser appUser = appUserMapper.selectById(userId);
        if (ObjectUtil.isNull(appUser)) {
            throw new ServiceException("用户不存在, 请重新登录或联系前台", 10001);
        } else if (StringUtils.equals("1", appUser.getStatus())) {
            throw new ServiceException("用户已停用, 请联系前台", 10001);
        }
        AppUserScoreVo userScoreVo = new AppUserScoreVo();
        if (StringUtils.isBlank(appUser.getCardId())) {
            userScoreVo.setScoreTotal(BigDecimal.ZERO.toPlainString());
            userScoreVo.setScoreUsable(BigDecimal.ZERO.toPlainString());
        } else {
            AppGuestScore guestScore = appGuestScoreMapper.selectById(appUser.getCardId());
            if (ObjectUtil.isNull(guestScore)) {
                userScoreVo.setScoreTotal(BigDecimal.ZERO.toPlainString());
                userScoreVo.setScoreUsable(BigDecimal.ZERO.toPlainString());
            } else {
                userScoreVo.setScoreTotal(guestScore.getScoreTotal().toPlainString());
                userScoreVo.setScoreUsable(guestScore.getScoreUsable().toPlainString());
            }
        }
        AppUserVo userVo = BeanUtil.copyProperties(appUser, AppUserVo.class);
        userVo.setUserScore(userScoreVo);
        return userVo;
    }

    /**
     * 获取旅客入住信息
     *
     * @param roomId 房间编号
     * @param userId 用户编号
     */
    @Override
    public AppGuestVo getGuestInfo(Long roomId, Long userId) {
        AppUser appUser = appUserMapper.selectById(userId);
        if (ObjectUtil.isNull(appUser)) {
            throw new ServiceException("用户不存在, 请重新登录或联系前台", 10001);
        } else if (StringUtils.equals("1", appUser.getStatus())) {
            throw new ServiceException("用户已停用, 请联系前台", 10001);
        }
        // 查找房间当前是否入住
        AppRoom appRoom = appRoomMapper.selectById(roomId);
        if (ObjectUtil.isNull(appRoom)) {
            throw new ServiceException("房间暂时不可入住, 请重试或联系前台");
        } else if (!StringUtils.equals("2", appRoom.getStatus())) {
            throw new ServiceException("房间暂未办理入住, 请重试或联系前台");
        } else if (ObjectUtil.isNull(appRoom.getRoomRecordId())) {
            throw new ServiceException("房间暂未登记入住, 请重试或联系前台");
        }
        // 通过入住登记记录来对比身份证号是否一致
        AppRoomRecord appRoomRecord = appRoomRecordMapper.selectOne(
            Wrappers.lambdaQuery(AppRoomRecord.class)
                .eq(AppRoomRecord::getId, appRoom.getRoomRecordId())
                .eq(AppRoomRecord::getRoomId, appRoom.getId())
        );
        if (ObjectUtil.isNull(appRoomRecord)) {
            throw new ServiceException("房间入住登记查询失败, 请重试或联系前台");
        }
        if (StringUtils.isBlank(appUser.getCardId())) {
            // 查找旅客信息
            AppGuest appGuest = appGuestMapper.selectById(appRoomRecord.getCardId());
            return BeanUtil.copyProperties(appGuest, AppGuestVo.class);
        } else {
            if (!StringUtils.endsWithIgnoreCase(appUser.getCardId(), appRoomRecord.getCardId())) {
                throw new ServiceException("该房间用户信息已绑定他人, 如需变更请联系前台");
            } else {
                throw new ServiceException("已绑定, 请勿重复操作");
            }
        }
    }

    /**
     * 确认绑定身份证号
     *
     * @param roomId 房间编号
     * @param userId 用户编号
     * @return 结果
     */
    @Override
    public Boolean bindingUserByCard(Long roomId, Long userId) {
        AppRoom appRoom = appRoomMapper.selectById(roomId);
        // 通过入住登记记录来对比身份证号是否一致
        AppRoomRecord appRoomRecord = appRoomRecordMapper.selectOne(
            Wrappers.lambdaQuery(AppRoomRecord.class)
                .eq(AppRoomRecord::getId, appRoom.getRoomRecordId())
                .eq(AppRoomRecord::getRoomId, appRoom.getId())
        );
        String cardIdStr = appRoomRecord.getCardId();
        int rows = appUserMapper.update(null,
            Wrappers.lambdaUpdate(AppUser.class)
                .set(AppUser::getCardId, cardIdStr)
                .set(AppUser::getUpdateTime, DateUtils.getNowDate())
                .set(AppUser::getUpdateBy, String.valueOf(userId) + "_user_card_update")
                .eq(AppUser::getId, userId)
        );
        return rows > 0;
    }

    /**
     * 获取房间信息
     *
     * @param roomId 房间编号
     * @return 房间信息
     */
    @Override
    public AppRoomVo getRoomInfo(Long roomId, Long userId) {
        AppUser appUser = appUserMapper.selectById(userId);
        if (ObjectUtil.isNull(appUser)) {
            throw new ServiceException("用户不存在, 请重新登录或联系前台", 10001);
        } else if (StringUtils.equals("1", appUser.getStatus())) {
            throw new ServiceException("用户已停用, 请联系前台", 10001);
        }
        // 查找房间当前是否入住
        AppRoom appRoom = appRoomMapper.selectById(roomId);
        if (ObjectUtil.isNull(appRoom)) {
            throw new ServiceException("房间暂时不可入住, 请重试或联系前台");
        } else if (!StringUtils.equals("2", appRoom.getStatus())) {
            throw new ServiceException("房间暂未办理入住, 请重试或联系前台");
        } else if (ObjectUtil.isNull(appRoom.getRoomRecordId())) {
            throw new ServiceException("房间暂未登记入住, 请重试或联系前台");
        }
        // 通过入住登记记录来对比身份证号是否一致
        AppRoomRecord appRoomRecord = appRoomRecordMapper.selectOne(
            Wrappers.lambdaQuery(AppRoomRecord.class)
                .eq(AppRoomRecord::getId, appRoom.getRoomRecordId())
                .eq(AppRoomRecord::getRoomId, appRoom.getId())
        );
        if (ObjectUtil.isNull(appRoomRecord)) {
            throw new ServiceException("房间入住登记查询失败, 请重试或联系前台");
        }
        if (StringUtils.isNotBlank(appUser.getCardId()) &&
            !StringUtils.endsWithIgnoreCase(appUser.getCardId(), appRoomRecord.getCardId())) {
            throw new ServiceException("该房间用户信息已绑定他人, 如需变更请联系前台");
        }
        // 查找房间物品列表
        List<AppGoods> goodsList = appGoodsMapper.selectList(
            Wrappers.lambdaQuery(AppGoods.class)
                .eq(AppGoods::getRoomId, roomId)
                .eq(AppGoods::getStatus, "1")
                .eq(AppGoods::getDelFlag, "0")
        );
        List<AppGoodsVo> goodsVoList = new ArrayList<AppGoodsVo>();
        if (CollUtil.isNotEmpty(goodsList)) {
            // 物品类型
            List<AppCategory> categoryList = appCategoryMapper.selectList();
            for (AppGoods goods : goodsList) {
                AppGoodsVo goodsVo = BeanUtil.copyProperties(goods, AppGoodsVo.class);
                Long _categoryId = goods.getCategoryId();
                if (ObjectUtil.isNotNull(_categoryId)) {
                    AppCategory category = categoryList.stream().filter(m -> ObjectUtil.equal(m.getId(), _categoryId)).findFirst().orElse(null);
                    if (ObjectUtil.isNotNull(category)) goodsVo.setCategoryName(category.getName());
                }
                goodsVoList.add(goodsVo);
            }
        }
        // 返回房间信息
        AppRoomVo roomVo = BeanUtil.copyProperties(appRoom, AppRoomVo.class);
        roomVo.setGoods(goodsVoList);
        return roomVo;
    }
}
