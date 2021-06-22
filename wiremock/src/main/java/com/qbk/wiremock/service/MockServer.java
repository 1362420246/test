package com.qbk.wiremock.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * @author ：quboka
 * @description： WireMock 模拟数据
 * @date ：2019/12/5 19:05
 */
public class MockServer {

    public static void main(String[] args) throws IOException {

        //一、下载 wiremock 独立服务  http://wiremock.org/docs/running-standalone/

        //二、启动wiremock 并指定端口  java -jar wiremock-standalone-2.25.1.jar --port 8062

        //配置wiremock端口  连接wiremocek服务
        WireMock.configureFor(8062);

        //清空以前的配置
        WireMock.removeAllMappings();

        mock("/order/1", "01");
        mock("/order/2", "02");
    }

    private static void mock(String url, String file) throws IOException {
        //加载 文件
        ClassPathResource resource = new ClassPathResource("mock/response/" + file + ".txt");
        //读取文件中的内容
        String content = StringUtils
                //拼接
                .join(//将文件内容逐行读取到字符串列表中
                        FileUtils.readLines(resource.getFile(), "UTF-8").toArray(),
                        "\n");
        //伪造数据
        WireMock.stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withBody(content).withStatus(200)));

    }

}
