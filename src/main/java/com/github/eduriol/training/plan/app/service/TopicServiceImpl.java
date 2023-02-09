package com.github.eduriol.training.plan.app.service;

import com.github.eduriol.training.plan.app.dao.ITopicDao;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TopicServiceImpl implements ITopicService {

    @Autowired
    private ITopicDao topicDao;

    @Override
    @Transactional
    public Topic save(Topic topic) {
        return topicDao.save(topic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Topic> findAll() {
        return (List<Topic>) topicDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Topic findById(Long id) {
        return topicDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        topicDao.deleteById(id);
    }
}
