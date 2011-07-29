package edu.cornell.pserc.jpower.tdouble;

import edu.cornell.pserc.jpower.tdouble.cases.Djp_case4gs;
import edu.cornell.pserc.jpower.tdouble.jpc.Djp_jpc;
import edu.cornell.pserc.jpower.tdouble.pf.Djp_rundcpf;

public class Djp_main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Djp_jpc jpc = Djp_case4gs.jp_case4gs();
		Djp_rundcpf.jp_rundcpf(jpc);
	}

}
