package org.cent.dynamicdatasourcedemo.service;

import org.springframework.stereotype.Service;

/**
 * @author Vincent
 * @version 1.0 2019/11/23
 */
@Service
public interface CounterService {
    String getId(String name);
}
