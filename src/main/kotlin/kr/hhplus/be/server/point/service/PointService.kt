package kr.hhplus.be.server.point.service

import kr.hhplus.be.server.point.controller.dto.ChargeRes
import kr.hhplus.be.server.point.controller.dto.GetPointRes
import org.springframework.stereotype.Service

@Service
class PointService {
    fun chargePoint(userId: Long, amount: Int): ChargeRes {
        return ChargeRes(1,10000)
    }

    fun getPoint(userId: Long): GetPointRes {
        return GetPointRes(1,10000)
    }


}
