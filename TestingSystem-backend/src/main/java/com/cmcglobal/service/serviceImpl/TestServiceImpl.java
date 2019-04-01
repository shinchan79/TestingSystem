package com.cmcglobal.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcglobal.entity.Candidate;
import com.cmcglobal.entity.CandidateTest;
import com.cmcglobal.entity.SemesterExam;
import com.cmcglobal.entity.Test;
import com.cmcglobal.entity.User;
import com.cmcglobal.repository.CandidateRepository;
import com.cmcglobal.repository.CandidateTestRepository;
import com.cmcglobal.repository.SemesterExamRepository;
import com.cmcglobal.repository.TestRepository;
import com.cmcglobal.repository.UserRepository;
import com.cmcglobal.service.ServiceResult;
import com.cmcglobal.service.ServiceResult.Status;
import com.cmcglobal.service.TestService;

@Service
@Transactional
public class TestServiceImpl implements TestService {

	@Autowired
	private TestRepository testRepository;
	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SemesterExamRepository semesterExamRepository;
	@Autowired
	private CandidateTestRepository candidateTestRepository;

	@Override
	public List<Test> findAll() {
		// TODO Auto-generated method stub
		return testRepository.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmcglobal.service.TestService#insertTest(com.cmcglobal.entity.Test)
	 */
	@Override
	public ServiceResult insertTest(Test test) {
		ServiceResult result = new ServiceResult();
		result.setData(testRepository.save(test));
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmcglobal.service.TestService#deleteTestID(java.lang.Integer)
	 */
	@Override
	public ServiceResult deleteTest(Integer id) {
		ServiceResult result = new ServiceResult();
		Test test = testRepository.findById(id).orElse(null);
		if (test == null) {
			result.setMessage("Not Found object");
			result.setStatus(Status.FAILED);
		} else {
			testRepository.delete(test);
			result.setStatus(Status.SUCCESS);
			result.setData(testRepository.findAll());
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmcglobal.service.TestService#findOne(java.lang.Integer)
	 */
	@Override
	public Test findOne(Integer id) {
		return testRepository.findById(id).get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmcglobal.service.TestService#findBySemesterID(java.lang.String)
	 */
	@Override
	public ServiceResult findBySemesterID(String id) {
		ServiceResult result = new ServiceResult();
		List<Test> test = testRepository.findAll();
		List<Test> test2 = new ArrayList<>();
		for (Test test3 : test) {
			if (test3.getSemesterExam().getId().equals(id)) {
				test2.add(test3);
			}
		}
		result.setData(test2);
		return result;
	}

	@Override
	public Test getTestById(int id) {
		Test test = testRepository.getOne(id);
		return test;
	}

	@Override
	public ServiceResult getTestsOfSemesterOfUser(int userId, String semesterId) {
		ServiceResult result = new ServiceResult();
		Optional<SemesterExam> exam = semesterExamRepository.findById(semesterId);
		if (exam.isPresent()) {
			SemesterExam semesterExam = exam.get();
			List<Test> testList = testRepository.findBySemesterExam(semesterExam);
			if (testList != null) {
				result.setData(testList);
				User user = userRepository.getOne(userId);
				List<Candidate> candidateList = candidateRepository.findByUser(user);
				
				List<CandidateTest> candidateTestList = candidateTestRepository.findAll();
				System.out.println(candidateTestList);
				testList = new ArrayList<Test>();

				for (Candidate candidate : candidateList) {
					for (CandidateTest candidateTest : candidateTestList) {
						if (candidateTest.getCandidates().getId() == candidate.getId()) {
							testList.add(candidateTest.getTests());
							
							
						}
					}
				}

			}
		}

		return result;
	}

	@Override
	public ServiceResult getTestBySemesterIdAndExamId(String semesterId, String examId) {
		ServiceResult result = new ServiceResult();
		List<Test> tests = testRepository.findAll();
		for (Test test : tests) {
			if (test.getSemesterExam().getId().equals(semesterId) && test.getExam().getExamId().equals(examId)) {
				result.setData(test);
				result.setHttpStatus(HttpStatus.OK);
				return result;
			}
		}
		result.setHttpStatus(HttpStatus.NOT_FOUND);
		return result;
	}

}