package com.genie.core.utils;

import javax.script.*;
import java.util.Map;

/**
 * JS 脚本引擎
 * Created by meng013 on 2017/12/15.
 */
public class JsUtil {

    static ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");

    /**
     * 执行js脚本
     * @param jsStr
     * @param args
     * @return
     * @throws ScriptException
     */
    public static Object run (String jsStr, Map<String, Object> args) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        Compilable compilable = (Compilable) engine;
        CompiledScript JSFunction = compilable.compile(jsStr); //解析编译脚本函数

        Bindings bindings = engine.createBindings(); //Local级别的Binding
        for(Map.Entry<String, Object> entry : args.entrySet()){
            bindings.put(entry.getKey(), entry.getValue());
        }
        return JSFunction.eval(bindings);
    }

    /**
     * 执行编译后的js脚本
     * @param JSFunction
     * @param args
     * @return
     * @throws ScriptException
     */
    public static Object run (CompiledScript JSFunction, Map<String, Object> args) throws ScriptException {

        Bindings bindings = engine.createBindings(); //Local级别的Binding
        for(Map.Entry<String, Object> entry : args.entrySet()){
            bindings.put(entry.getKey(), entry.getValue());
        }
        return JSFunction.eval(bindings);
    }

    /**
     * 编辑js脚本
     * @param jsStr
     * @return
     * @throws ScriptException
     */
    public static CompiledScript compileScript(String jsStr) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        Compilable compilable = (Compilable) engine;
        CompiledScript JSFunction = compilable.compile(jsStr); //解析编译脚本函数
        return JSFunction;
    }
}
