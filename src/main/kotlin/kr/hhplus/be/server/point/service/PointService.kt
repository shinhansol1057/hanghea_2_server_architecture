package kr.hhplus.be.server.point.service

import kr.hhplus.be.server.point.controller.dto.ChargeRes
import org.springframework.stereotype.Service

@Service
class PointService {
    fun chargePoint(userId: Long, amount: Int): ChargeRes {
        return ChargeRes(1,10000)
    }


}
