package com.cmcglobal.service;

import java.util.List;

import com.cmcglobal.entity.Test;

/**
 * @author User
 *
 */
public interface TestService {

	/**
	 * Create by: VuThuy - CMC
	 * Create date: Feb 18, 2019
	 * Modifier: VuThuy
	 * Modified date: Feb 18, 2019
	 * Description: ....
	 * Version 1.0
	 * 
	 * @return
	 */
	List<Test> findAll();

	/**
	 * Create by: VuThuy - CMC
	 * Create date: Feb 18, 2019
	 * Modifier: VuThuy
	 * Modified date: Feb 18, 2019
	 * Description: ....
	 * Version 1.0
	 * 
	 * @param test
	 */
	public ServiceResult insertTest(Test test);

	/**
	 * Create by: VuThuy - CMC
	 * Create date: Feb 18, 2019
	 * Modifier: VuThuy
	 * Modified date: Feb 18, 2019
	 * Description: ....
	 * Version 1.0
	 * 
	 * @param id
	 */
	public ServiceResult deleteTest(Integer id);

	/**
	 * Create by: VuThuy - CMC
	 * Create date: Feb 19, 2019
	 * Modifier: VuThuy
	 * Modified date: Feb 19, 2019
	 * Description: ....
	 * Version 1.0
	 * 
	 * @param id
	 * @return
	 */
	Test findOne(Integer id);

	/**
	 * Create by: VuThuy - CMC
	 * Create date: Feb 19, 2019
	 * Modifier: VuThuy
	 * Modified date: Feb 19, 2019
	 * Description: findBySemesterID in table test.
	 * Version 1.0
	 * 
	 * @param id
	 * @return
	 */
	ServiceResult findBySemesterID(String id);

	/**
	 * Create by: nvdiem - CMC
	 * Create date: Feb 23, 2019
	 * Modifier: nvdiem -CMC
	 * Modified date: Feb 23, 2019
	 * Description: ....
	 * Version 1.0
	 * 
	 * @param id id of test
	 * @return
	 */
	Test getTestById(int id);

	/**
	 * Create by: nvdiem - CMC
	 * Create date: Feb 23, 2019
	 * Modifier: nvdiem -CMC
	 * Modified date: Feb 23, 2019
	 * Description: ....
	 * Version 1.0
	 * 
	 * @param userId
	 * @param semesterId
	 * @return
	 */
	ServiceResult getTestsOfSemesterOfUser(int userId, String semesterId);

	/**
	 * Create by: nvdiem - CMC
	 * Create date: Feb 23, 2019
	 * Modifier: nvdiem -CMC
	 * Modified date: Feb 23, 2019
	 * Description: ....
	 * Version 1.0
	 * 
	 * @param semesterId
	 * @param examId
	 * @return
	 */
	ServiceResult getTestBySemesterIdAndExamId(String semesterId, String examId);
	
}
