package com.cmcglobal.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.cmcglobal.entity.SemesterExam;
import com.cmcglobal.entity.SemesterInformation;
import com.cmcglobal.repository.CandidateRepository;
import com.cmcglobal.service.SemesterExamService;
import com.cmcglobal.service.ServiceResult;
import com.cmcglobal.service.serviceImpl.SemesterExamServiceImpl;
import com.cmcglobal.utils.Api;

@CrossOrigin(origins = Api.BASE_URL_CORS, maxAge = 3600)
@RestController
@RequestMapping(value = Api.SemesterExam.API_URL_SEMESTER_EXAM_START)
public class SemesterExamController {
	@Autowired
	private SemesterExamService examService;
	@Autowired
	private SemesterExamServiceImpl semesterexamService;

	@Autowired
	private CandidateRepository candidateRepository;

	/**
	 * Create by: dvthuan - CMC Create date: Feb 16, 2019 Modifier: dvthuan Modified
	 * date: Feb 16, 2019 Description: GET ALL SEMESTER Exam Service Version 1.0
	 * 
	 * @return
	 */
	@GetMapping(value = Api.SemesterExam.API_URL_FILL_ALL_SEMESTER_EXAM)
	public ResponseEntity<ServiceResult> findAllSemesterExam() {
		return new ResponseEntity<ServiceResult>(examService.getAllSemesterExam(), HttpStatus.OK);
	}

	/**
	 * Create by: dvthuan - CMC Create date: Feb 16, 2019 Modifier: dvthuan Modified
	 * date: Feb 16, 2019 Description: CREATE NEW SEMESTER Exam Version 1.0
	 * 
	 * @param semesterExam
	 * @return
	 */
	@PostMapping(value = Api.SemesterExam.API_URL_CREATE_SEMESTER_EXAM)
	public ResponseEntity<ServiceResult> create(@RequestBody SemesterExam semesterExam) {
//		semesterExam.setCreator("1");

		return new ResponseEntity<ServiceResult>(examService.create(semesterExam), HttpStatus.OK);
	}

	/**
	 * Create by: dvthuan - CMC Create date: Feb 16, 2019 Modifier: dvthuan Modified
	 * date: Feb 16, 2019 Description: SEARCH SEMESTER Exam Version 1.0
	 * 
	 * @param keyword
	 * @return
	 */
	@PostMapping(value = Api.SemesterExam.API_URL_SEARCH_SEMESTER_EXAM)
	public ResponseEntity<ServiceResult> search(@RequestBody String keyword) {
		return new ResponseEntity<ServiceResult>(examService.search(keyword), HttpStatus.OK);
	}

	/**
	 * Create by: dvthuan - CMC Create date: Feb 16, 2019 Modifier: dvthuan Modified
	 * date: Feb 16, 2019 Description: DELETE SEMESTER Exam Version 1.0
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping(value = Api.SemesterExam.API_URL_DELETE_SEMESTER_EXAM)
	public ResponseEntity<ServiceResult> delete(@RequestBody String id) {
		return new ResponseEntity<ServiceResult>(examService.delete(id), HttpStatus.OK);
	}

	/**
	 * Create by: dvthuan - CMC Create date: Feb 18, 2019 Modifier: dvthuan Modified
	 * date: Feb 18, 2019 Description: get one by id Version 1.0
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = Api.SemesterExam.API_URL_GET_ONE_SEMESTER_EXAM)
	public ResponseEntity<ServiceResult> findById(@PathVariable String id) {
		return new ResponseEntity<ServiceResult>(examService.findById(id), HttpStatus.OK);
	}

	/**
	 * Create by: dvthuan - CMC Create date: Feb 22, 2019 Modifier: dvthuan Modified
	 * date: Feb 22, 2019 Description: .... Version 1.0
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping(value = Api.SemesterExam.API_URL_FILTER_SEMESTER_EXAM)
	public ResponseEntity<ServiceResult> filterByKeyword(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "fullname", required = false) String fullname,
			@RequestParam(value = "", required = false) Date startTime,
			@RequestParam(value = "", required = false) Date endTime) {

		System.err.println("Fullname: " + fullname);
		return new ResponseEntity<ServiceResult>(examService.filter(name, status, fullname, startTime, endTime),
				HttpStatus.OK);
	}

	/**
	 * Create by: pvhao - CMC Create date: Feb 18, 2019 Modifier: User Modified
	 * date: Feb 18, 2019 Description: .... Version 1.0
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(Api.SemesterExam.API_URL_REPORT_SEMESTER_EXAM)
	public ResponseEntity<?> getInformationOfSemester(@PathVariable("id") String id) {
		SemesterInformation semesterInformation = semesterexamService.getInformationOfSemester(id);
		if (semesterInformation == null) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<>(semesterInformation, HttpStatus.OK);

	}

	/**
	 * Create by: nvdiem - CMC Create date: Feb 25, 2019 Modifier: nvdiem -CMC
	 * Modified date: Feb 25, 2019 Description: .... Version 1.0
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = Api.SemesterExam.API_URL_GET_SEMESTER_LIST_BY_USER_ID, method = RequestMethod.GET)
	public ResponseEntity<ServiceResult> getSemesterListByUserId(@PathVariable("id") int id) {
		return new ResponseEntity<ServiceResult>(examService.getSemesterListByUserId(id), HttpStatus.OK);
	}

	@PostMapping(value = "/filter")
	@ResponseStatus(code = HttpStatus.OK)
	public List<SemesterExam> filterSemesterExam(@RequestBody SemesterExam semesterExam) {
		return examService.getAll(semesterExam);
	}
}
