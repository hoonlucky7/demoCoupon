package com.example.demo.coupon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduleComponent {

    @Autowired
    CouponService couponService;
    //Scheduler works every 1 am in KST
    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduleExpiredCoupon() {
        log.info("scheduleExpiredCoupon start");
        //couponService.getExpiredByToday();
        log.info("scheduleExpiredCoupon finish");
    }

    //Scheduler works every 8 am in KST
    @Scheduled(cron = "0 0 8 * * ?")
    public void scheduleExpiredAfter3days() {
        log.info("scheduleExpiredAfter3day start");
        couponService.sendExpiredAfter3days();
        log.info("scheduleExpiredAfter3day finish");
    }
}
