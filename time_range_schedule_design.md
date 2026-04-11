# 时间范围调度功能设计文档

## 1. 功能需求

### 1.1 需求描述

支持从某个时间点到另一个时间点之间，每隔多少秒、每隔多少分钟、每隔多少小时触发任务。

### 1.2 需求分析

- **时间范围**：需要指定开始时间和结束时间
- **间隔单位**：支持秒、分钟、小时三种单位
- **间隔值**：用户可自定义间隔数值
- **触发规则**：在时间范围内，按照指定间隔触发任务

## 2. 技术设计

### 2.1 调度类型设计

在现有的 `ScheduleTypeEnum` 中添加新的调度类型 `TIME_RANGE`。

### 2.2 配置格式设计

新的调度类型配置格式采用 JSON 格式，包含以下字段：

```json
{
  "startTime": "2023-12-01 00:00:00",  // 开始时间
  "endTime": "2023-12-31 23:59:59",    // 结束时间
  "interval": 5,                       // 间隔值
  "intervalUnit": "minute"            // 间隔单位：second, minute, hour
}
```

### 2.3 核心实现

#### 2.3.1 新增调度类型枚举

在 `ScheduleTypeEnum` 中添加 `TIME_RANGE` 枚举值：

```java
TIME_RANGE(I18nUtil.getString("schedule_type_time_range"), new TimeRangeScheduleType()),
```

#### 2.3.2 实现 TimeRangeScheduleType 类

创建 `TimeRangeScheduleType` 类，继承自 `ScheduleType`，实现 `generateNextTriggerTime` 方法：

```java
public class TimeRangeScheduleType extends ScheduleType {

    @Override
    public Date generateNextTriggerTime(JobInfo jobInfo, Date fromTime) throws Exception {
        // 解析配置
        String scheduleConf = jobInfo.getScheduleConf();
        TimeRangeConfig config = JSON.parseObject(scheduleConf, TimeRangeConfig.class);
        
        // 解析开始时间和结束时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = sdf.parse(config.getStartTime());
        Date endTime = sdf.parse(config.getEndTime());
        
        // 检查当前时间是否在时间范围内
        if (fromTime.before(startTime)) {
            return startTime;
        }
        if (fromTime.after(endTime)) {
            return null; // 超出时间范围，不再触发
        }
        
        // 计算下一次触发时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromTime);
        
        switch (config.getIntervalUnit()) {
            case "second":
                calendar.add(Calendar.SECOND, config.getInterval());
                break;
            case "minute":
                calendar.add(Calendar.MINUTE, config.getInterval());
                break;
            case "hour":
                calendar.add(Calendar.HOUR_OF_DAY, config.getInterval());
                break;
            default:
                throw new IllegalArgumentException("Invalid interval unit: " + config.getIntervalUnit());
        }
        
        Date nextTime = calendar.getTime();
        
        // 检查下一次触发时间是否在时间范围内
        if (nextTime.after(endTime)) {
            return null; // 超出时间范围，不再触发
        }
        
        return nextTime;
    }
}
```

#### 2.3.3 定义 TimeRangeConfig 类

创建 `TimeRangeConfig` 类，用于解析和存储时间范围调度的配置：

```java
public class TimeRangeConfig {
    private String startTime;
    private String endTime;
    private int interval;
    private String intervalUnit;
    
    // getters and setters
}
```

### 2.4 前端界面设计

在任务编辑页面，为 `TIME_RANGE` 调度类型添加对应的配置表单：

- 开始时间选择器
- 结束时间选择器
- 间隔值输入框
- 间隔单位下拉选择（秒、分钟、小时）

### 2.5 数据存储

利用现有的 `schedule_conf` 字段存储 JSON 格式的配置信息。

## 3. 实现步骤

1. **添加国际化资源**：在 `message_zh_CN.properties` 和 `message_en.properties` 中添加 "schedule_type_time_range" 对应的翻译

2. **实现核心类**：
   - 创建 `TimeRangeConfig.java`
   - 创建 `TimeRangeScheduleType.java`
   - 修改 `ScheduleTypeEnum.java` 添加新的调度类型

3. **更新前端界面**：
   - 修改任务编辑页面，添加时间范围调度的配置表单
   - 添加时间范围调度类型的选项

4. **测试验证**：
   - 测试不同时间范围和间隔的调度场景
   - 验证边界情况（如开始时间等于结束时间）
   - 验证超出时间范围后是否停止触发

## 4. 代码修改清单

### 4.1 后端修改

1. **`schedule-admin/src/main/java/com/zvosframework/schedule/admin/scheduler/type/ScheduleTypeEnum.java`**
   - 添加 `TIME_RANGE` 枚举值

2. **`schedule-admin/src/main/java/com/zvosframework/schedule/admin/scheduler/type/strategy/TimeRangeScheduleType.java`**
   - 实现时间范围调度逻辑

3. **`schedule-admin/src/main/java/com/zvosframework/schedule/admin/scheduler/type/strategy/TimeRangeConfig.java`**
   - 定义时间范围调度配置

4. **`schedule-admin/src/main/resources/i18n/message_zh_CN.properties`**
   - 添加 "schedule_type_time_range" 翻译

5. **`schedule-admin/src/main/resources/i18n/message_en.properties`**
   - 添加 "schedule_type_time_range" 翻译

### 4.2 前端修改

1. **`schedule-admin/src/main/resources/templates/biz/job.list.ftl`**
   - 在调度类型下拉框中添加时间范围选项

2. **`schedule-admin/src/main/resources/static/biz/jobinfo/jobinfo.edit.js`**
   - 添加时间范围调度的配置表单
   - 实现配置的序列化和反序列化

## 5. 测试用例

### 5.1 测试场景

1. **基本功能测试**：
   - 配置从当前时间开始，10秒后结束，每隔2秒触发一次
   - 验证任务是否在时间范围内按间隔触发
   - 验证时间范围结束后是否停止触发

2. **边界测试**：
   - 开始时间等于结束时间
   - 间隔值为0
   - 无效的间隔单位

3. **不同间隔单位测试**：
   - 测试秒级间隔
   - 测试分钟级间隔
   - 测试小时级间隔

4. **时间范围跨越测试**：
   - 测试跨天的时间范围
   - 测试跨月的时间范围

## 6. 兼容性考虑

- 新的调度类型不会影响现有的调度类型
- 系统会对配置进行验证，确保配置格式正确
- 对于无效的配置，系统会给出明确的错误提示

## 7. 性能考虑

- 时间范围调度的计算逻辑简单，不会对系统性能造成影响
- 对于大量任务的场景，系统已经有成熟的调度机制，可以有效处理

## 8. 总结

通过添加 `TIME_RANGE` 调度类型，系统将支持从某个时间点到另一个时间点之间，按照指定间隔触发任务的功能。这将为用户提供更灵活的任务调度选项，满足更多场景的需求。