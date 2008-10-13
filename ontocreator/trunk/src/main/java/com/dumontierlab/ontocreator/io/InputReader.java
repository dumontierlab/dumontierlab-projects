package com.dumontierlab.ontocreator.io;

import java.io.InputStream;

import com.dumontierlab.ontocreator.model.RecordSet;

public interface InputReader {

	RecordSet read(InputStream input, boolean isFirstRowHeader);
}
