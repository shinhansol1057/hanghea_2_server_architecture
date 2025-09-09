package kr.hhplus.be.server.coupon.service

import kr.hhplus.be.server.coupon.controller.dto.CouponListRes
import kr.hhplus.be.server.coupon.controller.dto.CouponRes
import org.springframework.stereotype.Service

@Service
class CouponService {
    fun getCouponList(userId: Long): CouponListRes {
        return CouponListRes(
            couponList = listOf()
        )
    }

    fun postLimitCoupon(userId: Long): CouponRes {
        return CouponRes(1, "선착순 쿠폰", 5000, "2025년 12월 31일 23시 59분 59초", false, "2025년 01월 01일 00시 00분 00초")
    }

}
