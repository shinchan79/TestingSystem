package com.cmcglobal.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "semester_exam")
public class SemesterExam {
	@Id
	@Column(name = "semester_exam_id")
	@GeneratedValue(generator = "auto_increase")
	@GenericGenerator(name = "auto_increase", strategy = "com.cmcglobal.utils.AutoIncreaseId")

	private String id;
	@Column(name = "semester_exam_name")
	private String name;
	@Column(name = "semester_description")
	private String description;

	@ManyToOne
	@JoinColumn(name = "create_by", nullable = false)
	@JsonIgnoreProperties("candidates")
	private User user;
	@Column(name = "status")
	private int status;
	@Column(name = "start_at")
	private Date startTime;
	@Column(name = "end_at")
	private Date endTime;
	@Column(name = "is_show")
    private int isShow;
	@Column(name = "created_date")
	@UpdateTimestamp
	private Date createdDate;
	@Transient
    private String roleName;
	@OneToMany(mappedBy = "semesterExam", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnoreProperties("semesterExam")
	private List<Test> tests;

	@OneToMany(mappedBy = "semesterExam", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnoreProperties("semesterExam")
	private List<Candidate> candidate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "semester_exam_id")
	@JsonIgnore
	private List<SemesterExamCode> semesterExamCode;
    
	public List<SemesterExamCode> getSemesterExamCode() {
		return semesterExamCode;
	}

	public void setSemesterExamCode(List<SemesterExamCode> semesterExamCode) {
		this.semesterExamCode = semesterExamCode;
	}

	public SemesterExam() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public Date getCreatedDate() {
		
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		
		this.createdDate = createdDate;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Test> getTests() {
		return tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}

	public List<Candidate> getCandidate() {
		return candidate;
	}

	public void setCandidate(List<Candidate> candidate) {
		this.candidate = candidate;
	}

}