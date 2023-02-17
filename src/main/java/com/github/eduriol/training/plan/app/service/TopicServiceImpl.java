package com.github.eduriol.training.plan.app.service;

import com.github.eduriol.training.plan.app.dao.ITopicDao;
import com.github.eduriol.training.plan.app.models.domain.Plan;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TopicServiceImpl implements ITopicService {

    private static final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);

    @Autowired
    private ITopicDao topicDao;

    @Override
    @Transactional
    public Topic save(Topic topic) {
        logger.debug("Saving topic data {}", topic);
        Topic savedTopic = topicDao.save(topic);
        logger.debug("Saved topic data {}", savedTopic);
        return savedTopic;
    }

    @Override
    @Transactional(readOnly = true)
    public Topic findByIdAndPlanId(Long id, Long planId) {
        logger.debug("Looking for topic with id = {} and plan id = {}", id, planId);
        Topic topic = topicDao.findByIdAndPlanId(id, planId);
        logger.debug("Found topic {}", topic);
        return topic;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        logger.debug("Deleting topic with id = {}", id);
        topicDao.deleteById(id);
        logger.debug("Topic deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Topic> findByPlan(Plan plan) {
        logger.debug("Looking for topics for plan with id = {}", plan.getId());
        List<Topic> topics = topicDao.findAllByPlan(plan);
        logger.debug("Found {} topics for plan with id = {}", topics.size(), plan.getId());
        return topics;
    }
}
