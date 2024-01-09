package com.heycine.slash.common.api;

import com.heycine.slash.common.basic.http.R;
import com.heycine.slash.common.domain.dto.ExampleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * AI任务-Feign
 *
 * @author zzj
 */
@FeignClient(contextId = "ExampleFeignClient", value = "common-service")
public interface ExampleFeignClient {

	/**
	 * 获取用户信息
	 * @param id
	 * @return
	 */
	@GetMapping({"/example/detail"})
	R<ExampleDTO> example(@RequestParam("id") String id);

}
