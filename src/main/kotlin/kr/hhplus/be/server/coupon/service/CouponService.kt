package kr.hhplus.be.server.coupon.service

import kr.hhplus.be.server.coupon.controller.dto.CouponListRes
import org.springframework.stereotype.Service

@Service
class CouponService {
    fun getCouponList(userId: Long): CouponListRes {
        return CouponListRes(
            couponList = listOf()
        )
    }

}
