package com.gbdpcloud;

import gbdpcloudsecurityapp.gbdpcloudsecurityapp.SecurityCoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients("gbdpcloudprovideruserapi.gbdpcloudprovideruserapi")
@SecurityCoreConfig
public class GbdpcloudProviderDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GbdpcloudProviderDemoApplication.class, args);
    }
}
