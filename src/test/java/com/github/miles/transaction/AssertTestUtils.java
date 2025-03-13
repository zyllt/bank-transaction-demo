
package com.github.miles.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.function.Consumer;

import com.github.miles.transaction.api.base.ApiCode;
import com.github.miles.transaction.api.base.BaseResponse;
import com.github.miles.transaction.api.base.PageResult;
/**
 * 断言工具类
 * 
 * @author miles.zeng
 * @since 2025-03-13
 */
public class AssertTestUtils {

    public static <T> Consumer<BaseResponse<T>> assertBaseResponse(T expectedData) {
        return assertBaseResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), expectedData);
    }


    public static <T> Consumer<BaseResponse<T>> assertBaseResponse(int expectedCode, String expectedMsg){
        return assertBaseResponse(expectedCode, expectedMsg, null);
    }

    public static <T> Consumer<BaseResponse<T>> assertBaseResponse(int expectedCode, String expectedMsg,
            T expectedData) {
        return response -> {
            assertNotNull(response);
            assertEquals(expectedCode, response.getCode());
            assertEquals(expectedMsg, response.getMsg());
            if(expectedData == null){
                assertNull(response.getData());
            }else{
                assertEquals(expectedData, response.getData());
            }
        };
    }

    public static <T> Consumer<BaseResponse<PageResult<T>>> assertBaseResponsePageResult(int expectedCode, String expectedMsg,
            PageResult<T> expectedData) {
        return response -> {
            assertNotNull(response);
            assertEquals(expectedCode, response.getCode());
            assertEquals(expectedMsg, response.getMsg()); 
            PageResult<T> data = response.getData();
            if(expectedData != null){
                assertNotNull(data);
                assertEquals(expectedData.getPage(), data.getPage());
                assertEquals(expectedData.getPageSize(), data.getPageSize());
                assertEquals(expectedData.getTotal(), data.getTotal());
                assertEquals(expectedData.getTotalPages(), data.getTotalPages());
                assertEquals(expectedData.getResults(), data.getResults());
            }else{
                assertNull(data);
            }
        };
    }



}
