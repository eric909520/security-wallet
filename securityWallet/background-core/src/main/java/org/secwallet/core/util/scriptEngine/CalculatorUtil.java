package org.secwallet.core.util.scriptEngine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * Digital calculation formula analysis budget tool class
 */
@Component
@Slf4j
public class CalculatorUtil  {
    /**
     * Rounded up
     * @param expre Evaluate expressions e.g.：a+b+c*d-e/(a-b) or 1+2-7+2+20/7+19%4
     * @param params variable value, e.g. params.put("a","12"),params.put("b","8")；If expre is 1+2-7+2+20/7+19%4 type formula, then params is null, otherwise null is not allowed
     * @param decimals Number of decimal places, for example: 2 means 2 decimal places are reserved after the decimal point
     * @param roundingEnum rounding mode, cannot be null
     * @return
     */
    public static double eval(String expre,Map<String,String> params,int decimals,RoundingEnum roundingEnum) {
        double resultValue=0.0000000000;
        BigDecimal obj =getBigDecimal(eval(expre, params));
        switch (roundingEnum){
            case ceil:
                resultValue=Math.ceil(obj.doubleValue());
                break;
            case floor:
                resultValue=Math.floor(obj.doubleValue());
                break;
            case round_down:
                resultValue=obj.setScale(decimals,BigDecimal.ROUND_DOWN).doubleValue();
                break;
            case round_up:
                resultValue=obj.setScale(decimals,BigDecimal.ROUND_UP).doubleValue();
                break;
            case round_floor:
                resultValue=obj.setScale(decimals,BigDecimal.ROUND_FLOOR).doubleValue();
                break;
            case round_ceiling:
                resultValue=obj.setScale(decimals,BigDecimal.ROUND_CEILING).doubleValue();
                break;
            case round_half_up:
                resultValue=obj.setScale(decimals,BigDecimal.ROUND_HALF_UP).doubleValue();
                break;
            case round_half_down:
                resultValue=obj.setScale(decimals,BigDecimal.ROUND_HALF_DOWN).doubleValue();
                break;
            case round_half_even:
                resultValue=obj.setScale(decimals,BigDecimal.ROUND_HALF_EVEN).doubleValue();
                break;
        }
        return resultValue;
    }


    /**
     * Evaluate expression
     * @param expre Evaluate expressions e.g：a+b+c*d-e/(a-b) or 1+2-7+2+20/7+19%4
     * @param params variable value, e.g. params.put("a","12"),params.put("b","8")；If expre is 1+2-7+2+20/7+19%4 type formula, then params is null, otherwise null is not allowed
     * @return
     */
    private static Object eval(String expre, Map<String,String> params){
        try{
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("nashorn");
            if (!params.isEmpty()){
                for (String key:params.keySet()) {
                    engine.put(key,params.get(key));
                }
            }
            Object result = engine.eval(expre);
            return result;
        }catch (ScriptException ex){
            log.error(ex.getMessage());
        }
        return null;
    }

    /**
     * data type conversion
     * @param value
     * @return
     */
    public static BigDecimal getBigDecimal( Object value ) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }

//   public static void main(String[] args) throws Exception
//    {
//        Map<String,String> params=new HashMap<>();
//        params.put("a","23.233");
//        params.put("b","12.334");
//
//        double hour = eval("if(a>b) parseFloat(a)+parseFloat(b)%2; else 2+4;",params,2,RoundingEnum.round_half_up);
//        System.out.println(hour);
//
//    }
}
