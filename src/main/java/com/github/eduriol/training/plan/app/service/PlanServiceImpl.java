package com.github.eduriol.training.plan.app.service;

import com.github.eduriol.training.plan.app.dao.IPlanDao;
import com.github.eduriol.training.plan.app.models.domain.Plan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlanServiceImpl implements IPlanService {

    private static final Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);

    @Autowired
    private IPlanDao planDao;

    @Override
    @Transactional
    public Plan save(Plan plan) {
        logger.debug("Saving plan data {}", plan);
        Plan savedPlan = planDao.save(plan);
        logger.debug("Saved plan data {}", savedPlan);
        return savedPlan;
    }

    @Override
    @Transactional(readOnly = true)
    public Plan findById(Long id) {
        logger.debug("Looking for plan with id = {}", id);
        Plan plan = planDao.findById(id).orElse(null);
        logger.debug("Found plan {}", plan);
        return plan;
    }

}
