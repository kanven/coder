<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${pkg}.${clazz}">

	<sql id="${clazz?uncap_first}">
		<#list fields as field>
			<#if field?index == 0>
				${field.column} ${field.field}
			<#else>
				,${field.column} ${field.field}
			</#if>
		</#list>
	</sql>
	
	<insert id="save" parameterType="${clazz?uncap_first}" flushCache="true"
		useGeneratedKeys="true">
		insert into
		${table}
		(
			<#assign num = 0>
			<#list fields as field>
				<#if field.primaryKey == false>
					<#if num == 0>
						${field.column}
					<#else>
						,${field.column}
					</#if>
					<#assign num++>
				</#if>
			</#list>
		)
		values(
			<#assign num = 0>
			<#list fields as field>
				<#if field.primaryKey == false>
					<#if num == 0>
						${"#{"}${field.field}}
					<#else>
						,${"#{"}${field.field}}
					</#if>
					<#assign num++>
				</#if>
			</#list>
		)
	</insert>
	
	<update id="update" parameterType="${clazz?uncap_first}" flushCache="true">
		update period_task
		<set>
			<#list fields as field>
				<#if field.primaryKey == false>
					<if test="${field.field} != null">${field.column}=${"#{"}${field.field}},</if>
				</#if>
			</#list>
		</set>
		where ${primaryKey.column} = ${"#{"}${primaryKey.field}}
	</update>
	
	<select id="get" parameterType="java.lang.Long" resultType="${clazz?uncap_first}">
		select
		<include refid="${clazz?capitalize}"></include>
		from ${table} where ${primaryKey.column} = ${"#{"}${primaryKey.field}}
	</select>
	
	<delete id="delete" parameterType="java.lang.Long" flushCache="true">
		delete from ${table} where ${primaryKey.column} = ${"#{"}${primaryKey.field}}
	</delete>

</mapper>