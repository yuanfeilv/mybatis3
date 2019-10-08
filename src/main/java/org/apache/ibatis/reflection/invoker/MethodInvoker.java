/**
 *    Copyright 2009-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.reflection.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.ibatis.reflection.Reflector;

/**
 * @author Clinton Begin
 */

/**
 * 指定方法的调用器
 */
public class MethodInvoker implements Invoker {
// 类型
  private final Class<?> type;
  // 方法
  private final Method method;

  /**
   * 默认的构造函数
   * @param method
   */
  public MethodInvoker(Method method) {
    this.method = method;
    // 如果参数个数为1 则设置type 为入参的参数类型。否则将其设为返回参数类型
    if (method.getParameterTypes().length == 1) {
      type = method.getParameterTypes()[0];
    } else {
      type = method.getReturnType();
    }
  }

  /**
   * 函数的调用方法
   * @param target 目标对象
   * @param args 参数
   * @return
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  @Override
  public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
    try {
      // 使用发射的invoke 机制进行调用
      return method.invoke(target, args);
    } catch (IllegalAccessException e) {
      // 如果不是public 修饰的方法，则将其Accessible 设为true
      if (Reflector.canControlMemberAccessible()) {
        method.setAccessible(true);
        //调用invoke 方法
        return method.invoke(target, args);
      } else {
        throw e;
      }
    }
  }

  @Override
  public Class<?> getType() {
    return type;
  }
}
