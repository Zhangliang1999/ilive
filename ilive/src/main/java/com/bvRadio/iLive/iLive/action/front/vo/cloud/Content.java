package com.bvRadio.iLive.iLive.action.front.vo.cloud;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias(value="content")
public class Content {

	@XStreamAlias(value="Inputs")
	List<Input> Inputs;
	
	@XStreamAlias(value="Outputs")
	List<Output> Outputs;


	public List<Input> getInputs() {
		return Inputs;
	}


	public void setInputs(List<Input> inputs) {
		Inputs = inputs;
	}


	public List<Output> getOutputs() {
		return Outputs;
	}


	public void setOutputs(List<Output> outputs) {
		Outputs = outputs;
	}
	
	
	
}
