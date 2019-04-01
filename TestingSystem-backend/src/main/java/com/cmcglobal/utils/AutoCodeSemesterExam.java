package com.cmcglobal.utils;

import java.io.Serializable;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class AutoCodeSemesterExam implements IdentifierGenerator {

	private String prefix = "codesemesterexam";

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
		String query = "SELECT e.id FROM SemesterExamCode e";
		Stream<String> ids = session.createQuery(query, String.class).stream();
		Long max = ids.map(o -> o.replace(prefix, "")).mapToLong(Long::parseLong).max().orElse(0L);
		System.err.println(prefix + (String.format("%04d", max + 1)));
		return prefix + (String.format("%04d", max + 1));
	}
}
