package com.github.eduriol.training.plan.app.service;

import com.github.eduriol.training.plan.app.models.domain.Topic;

import java.util.List;

public interface ITopicService {

    Topic save(Topic topic);

    List<Topic> findAll();

    Topic findById(Long id);

}
