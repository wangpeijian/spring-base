package com.wpj.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author wangpejian
 * @date 19-9-10 下午3:12
 */
@RestController
@RequestMapping("nio")
public class NioController {

    @RequestMapping("copy")
    public String copyFile() {

        try {
            FileInputStream in = new FileInputStream(new File("/home/wpj/Desktop/1.txt"));
            FileOutputStream out = new FileOutputStream(new File("/home/wpj/Desktop/2.txt"));

            FileChannel outC = out.getChannel();
            FileChannel inC = in.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (true) {

                System.out.println("每次读");

                //判断是否读完文件
                int eof = inC.read(buffer);
                if (eof == -1) {
                    break;
                }
                //重设一下buffer的position=0，limit=position
                buffer.flip();
                //开始写
                outC.write(buffer);
                //写完要重置buffer，重设position=0,limit=capacity
                buffer.clear();
            }
            inC.close();
            outC.close();
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("ok");
        return "ok";
    }

}
