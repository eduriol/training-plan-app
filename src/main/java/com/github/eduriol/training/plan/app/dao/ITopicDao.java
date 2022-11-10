package com.github.eduriol.training.plan.app.dao;

import com.github.eduriol.training.plan.app.models.domain.Topic;
import org.springframework.data.repository.CrudRepository;

public interface ITopicDao extends CrudRepository<Topic, Long> {
}
