/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ewide.photograph.app.event;

/**
 * author：XiongWei
 * date：2018/9/18 9:54
 * describe：
 */
public class EventCenter<T> {

    /**
     * reserved data
     */
    private T data;

    /**
     * 这个code要与其他事件的code保持不同
     */
    private int eventCode = -1;

    /**
     * @param eventCode eventCode不要自己定义，需要在EventCode中定义，以做区分
     */
    public EventCenter(int eventCode) {
        this(eventCode, null);
    }

    /**
     * @param eventCode eventCode不要自己定义，需要在EventCode中定义，以做区分
     * @param data
     */
    public EventCenter(int eventCode, T data) {
        this.eventCode = eventCode;
        this.data = data;
    }

    public EventCenter(T data) {
        this.data = data;
    }

    /**
     * 返回EventBus当前的Code
     *
     * @return
     */
    public int getEventCode() {
        return this.eventCode;
    }

    /**
     * get event reserved data
     *
     * @return
     */
    public T getData() {
        return this.data;
    }

}
