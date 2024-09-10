package com.revature.revhire.services;

import com.revature.revhire.models.Skills;
import com.revature.revhire.repositories.SkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillsService {

    @Autowired
    private SkillsRepository skillsRepository;

    public Skills createSkill(Skills skill) {
        return skillsRepository.save(skill);
    }

    public List<Skills> getAllSkills() {
        return skillsRepository.findAll();
    }
}
