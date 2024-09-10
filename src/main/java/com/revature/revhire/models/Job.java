package com.revature.revhire.models;

import com.revature.revhire.enums.ExperienceRequired;
import com.revature.revhire.enums.JobType;
import com.revature.revhire.enums.SalaryRange;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "job_title", nullable = false, length = 100)
    private String jobTitle;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "job_description", columnDefinition = "TEXT", nullable = false)
    private String jobDescription;

    @Column(name = "skills_required", nullable = false)
    private String skillsRequired;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "salary_range", nullable = false)
    private SalaryRange salaryRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_required", nullable = false)
    private ExperienceRequired experienceRequired;

    @Column(name = "street", nullable = false, length = 150)
    private String street;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "pincode", nullable = false, length = 10)
    private String pincode;

    @Column(name = "state", nullable = false, length = 100)
    private String state;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "post_date")
    private Date postDate;

    @Column(name = "end_date")
    private Date endDate;

//    @Lob
//    @Column(name = "company_logo", columnDefinition = "VARBINARY(MAX)", nullable = true)
//    private byte[] companyLogo;
//
//    public void setCompanyLogo(byte[] companyLogo) {
//        if (companyLogo != null && companyLogo.length > 0) {
//            this.companyLogo = companyLogo;
//        } else {
//            throw new IllegalArgumentException("Company logo cannot be empty.");
//        }
//    }


}
