package ${pkg};

<#list imports as impt>
import ${impt};
</#list>

@Alias("${clazz?uncap_first}")
public class ${clazz} implements Serializable {

<#list fields as field>
	/**
	* ${field.comment}
	*/
	private ${field.javaType} ${field.field};
</#list>

<#list fields as field>
	
	public void set${field.field?cap_first}(${field.javaType} ${field.field}){
		this.${field.field} = ${field.field};
	}
</#list>

<#list fields as field>
	
	public ${field.javaType} get${field.field?cap_first}(){
		return this.${field.field};
	}
</#list>

	@Override
	public String toString(){
		return "${clazz} [
			<#list fields as field>
			<#if field?index == 0>
				${field.field} = "+ this.${field.field}	+
			<#else>
				",${field.field} = "+ this.${field.field}	+
			</#if>
			</#list>
		"]";
	}	
	
}