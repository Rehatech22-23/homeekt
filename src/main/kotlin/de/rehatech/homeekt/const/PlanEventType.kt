package de.rehatech.homeekt.const

enum class PlanEventType(val id: Float) {
    NONE(0F),
    NODE_STATE_CHANGED(1F),
    NODE_ATTRIBUTE_CHANGE_SENT(2F),
    NODE_ATTRIBUTE_CHANGE_RECEIVED(3F),
    HOMEEGRAM_ACTION_NOT_EXECUTED(100F),
    HOMEEGRAM_TRIGGERED(101F),
    HOMEEGRAM_CANCELED(102F),
    HOMEEGRAM_ACTIVATED(103F),
    HOMEEGRAM_DEACTIVATED(104F),
    GROUP_SWITCHED(200F),
    SYSTEM_CUBE_ADDED(300F),
    SYSTEM_CUBE_REMOVED(301F),
    SYSTEM_USER_LOGIN_SUCCESSFUL(302F),
    SYSTEM_USER_LOGIN_FAILED(303F),
    SYSTEM_REBOOT(304F),
    SYSTEM_SHUTDOWN(305F),
    SYSTEM_START(306F),
    SYSTEM_UPDATE_STARTED(307F),
    SYSTEM_UPDATE_SUCCESSFUL(308F),
    SYSTEM_UPDATE_FAILED(309F),
    SYSTEM_INTERNET_CONNECTION_ESTABLISHED(310F),
    SYSTEM_INTERNET_CONNECTION_LOST(311F),
    SYSTEM_PROXY_CONNECTION_ESTABLISHED(312F),
    SYSTEM_PROXY_CONNECTION_LOST(313F),
    SYSTEM_WEATHER_UPDATE_SUCCESSFUL(314F),
    SYSTEM_WEATHER_UPDATE_FAILED(315F),
    WEBHOOK_SENT(400F),
    PUSH_SENT(500F),
    SYSTEM_BACKUP_CREATION_SUCCESSFUL(316F),
    SYSTEM_BACKUP_CREATION_FAILED(317F),
    SYSTEM_BACKUP_EXPORT_SUCCESSFUL(318F),
    SYSTEM_BACKUP_EXPORT_FAILED(319F),
    SYSTEM_HISTORY_EXPORT_SUCCESSFUL(320F),
    SYSTEM_HISTORY_EXPORT_FAILED(321F),
    SYSTEM_CUBE_UPDATE_STARTED(322F),
    SYSTEM_CUBE_UPDATE_SUCCESSFUL(323F),
    SYSTEM_CUBE_UPDATE_FAILED(324F),
    PLAN_ACTIVATED(600F),
    PLAN_DEACTIVATED(601F),
    PLAN_SCHEDULE_TRIGGERED(602F),
    PLAN_SCHEDULE_RESTORED(603F),
    PLAN_SCHEDULE_SKIPPED(604F),
    PLAN_EVENT_TRIGGERED(605F),
    PLAN_TEMPORARY_OVERRIDEN(606F),
    MLPRESENCE_DETECTION_ACTIVATED(700F),
    MLPRESENCE_DETECTION_DEACTIVATED(701F),
    MLPRESENCE_DETECTION_RESET(702F),
    MLPRESENCE_DETECTION_KNOWLEDGE_HAS_BEEN_RESET(703F),
    MLPRESENCE_DETECTION_OPERATING_MODE_CHANGED(704F),
    MLPRESENCE_DETECTION_VALUE_PREDICTED(705F),
    MLPRESENCE_DETECTION_TRAINING_STARTED(706F),
    MLPRESENCE_DETECTION_TRAINING_CANCELED(707F),
    MLPRESENCE_DETECTION_TRAINING_COMPLETED(708F)
}