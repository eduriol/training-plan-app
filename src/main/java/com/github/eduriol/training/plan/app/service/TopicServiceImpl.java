package com.github.eduriol.training.plan.app.service;

import com.github.eduriol.training.plan.app.dao.ITopicDao;
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
    public List<Topic> findAll() {
        logger.debug("Getting list of topics");
        List<Topic> topics = (List<Topic>) topicDao.findAll();
        logger.debug("Found {} topics", topics.size());
        return topics;
    }

    @Override
    @Transactional(readOnly = true)
    public Topic findById(Long id) {
        logger.debug("Looking for topic with id = {}", id);
        Topic topic = topicDao.findById(id).orElse(null);
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
}
