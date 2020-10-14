-- ----------------------------
-- Table structure for VISIT_INFO
-- ----------------------------
ALTER TABLE VISIT_INFO CHANGE `gmtCreate` `gmtCreate` TIMESTAMP NOT NULL;
ALTER TABLE VISIT_INFO CHANGE `gmtModified` `gmtModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
