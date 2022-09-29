package com.woodyside.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class TraceUnitRule implements AfterEachCallback, BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        log.info("**********************************************");
        log.info("****** 🚀 Start executing of the test: {}   From the group {} ******", context.getDisplayName(), context.getTags());
        log.info("**********************************************");
    }

    @Override
    public void afterEach(ExtensionContext context) {
        log.info("**********************************************");
        log.info("****** ❌ End executing of the test: {}  ******", context.getDisplayName());
        log.info("**********************************************");
    }
}
