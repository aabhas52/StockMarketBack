package com.project.stockmarket;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("JUnit Platform Suite")
@SelectPackages("com.project.stockmarket.testClasses")
public class TestSuite {
}

