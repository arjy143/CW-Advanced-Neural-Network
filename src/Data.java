public class Data {
	float T;
	float W;
	float SR;
	float DSP;
	float DRH;
	float PanE;
	
	public Data(float T, float W, float SR, float DSP, float DRH, float PanE) {
		this.T = T;
		this.W = W;
		this.SR = SR;
		this.DSP = DSP;
		this.DRH = DRH;
		this.PanE = PanE;			
	}
	
	public float getT() { 
		return T;
	}
	public float getW() { 
		return W;
	}
	public float getSR() { 
		return SR;
	}
	public float getDSP() { 
		return DSP;
	}
	public float getDRH() { 
		return DRH;
	}
	public float getPanE() { 
		return PanE;
	}
	
	public void setT(float t) { 
		this.T = t;
	}
	public void setW(float w) { 
		this.W = w;
	}
	public void setSR(float sr) { 
		this.SR = sr;
	}
	public void setDSP(float dsp) { 
		this.DSP = dsp;
	}
	public void setDRH(float drh) { 
		this.DRH = drh;
	}
	public void setPanE(float pane) { 
		this.PanE = pane;
	}
	@Override
	public String toString() { 
		return ("inputs: ("+this.getT()+", "+this.getW()+", "+this.getSR()+", "+this.getDSP()+", "+this.getDRH()+"), output:"+this.getPanE());
	}
}

