package com.dumontierlab.ontocreator.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpCommand {

	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
