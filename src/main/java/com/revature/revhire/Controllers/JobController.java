package com.revature.revhire.Controllers;

import com.revature.revhire.models.Job;
import com.revature.revhire.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

//    @PostMapping("/create")
//    public ResponseEntity<Job> createJob(
//            @RequestPart("job") Job job,
//            @RequestPart("companyLogo") MultipartFile companyLogo
//    ) throws IOException {
//        byte[] logoBytes = companyLogo.getBytes();
//        job.setCompanyLogo(logoBytes);
//        Job createdJob = jobService.createJob(job);
//        return ResponseEntity.ok(createdJob);
//    }

    @PostMapping("/create")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job createdJob = jobService.createJob(job);
        return ResponseEntity.ok(createdJob);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

}

