package mathLib.func;

import mathLib.integral.Integral1D;
import mathLib.integral.intf.IntegralFunction1D;

public class BesselFunc {
	protected static final double[] A_i0 = new double[]{-4.4153416464793395E-18D, 3.3307945188222384E-17D,
			-2.431279846547955E-16D, 1.715391285555133E-15D, -1.1685332877993451E-14D, 7.676185498604936E-14D,
			-4.856446783111929E-13D, 2.95505266312964E-12D, -1.726826291441556E-11D, 9.675809035373237E-11D,
			-5.189795601635263E-10D, 2.6598237246823866E-9D, -1.300025009986248E-8D, 6.046995022541919E-8D,
			-2.670793853940612E-7D, 1.1173875391201037E-6D, -4.4167383584587505E-6D, 1.6448448070728896E-5D,
			-5.754195010082104E-5D, 1.8850288509584165E-4D, -5.763755745385824E-4D, 0.0016394756169413357D,
			-0.004324309995050576D, 0.010546460394594998D, -0.02373741480589947D, 0.04930528423967071D,
			-0.09490109704804764D, 0.17162090152220877D, -0.3046826723431984D, 0.6767952744094761D};
	protected static final double[] B_i0 = new double[]{-7.233180487874754E-18D, -4.830504485944182E-18D,
			4.46562142029676E-17D, 3.461222867697461E-17D, -2.8276239805165836E-16D, -3.425485619677219E-16D,
			1.7725601330565263E-15D, 3.8116806693526224E-15D, -9.554846698828307E-15D, -4.150569347287222E-14D,
			1.54008621752141E-14D, 3.8527783827421426E-13D, 7.180124451383666E-13D, -1.7941785315068062E-12D,
			-1.3215811840447713E-11D, -3.1499165279632416E-11D, 1.1889147107846439E-11D, 4.94060238822497E-10D,
			3.3962320257083865E-9D, 2.266668990498178E-8D, 2.0489185894690638E-7D, 2.8913705208347567E-6D,
			6.889758346916825E-5D, 0.0033691164782556943D, 0.8044904110141088D};
	protected static final double[] A_i1 = new double[]{2.7779141127610464E-18D, -2.111421214358166E-17D,
			1.5536319577362005E-16D, -1.1055969477353862E-15D, 7.600684294735408E-15D, -5.042185504727912E-14D,
			3.223793365945575E-13D, -1.9839743977649436E-12D, 1.1736186298890901E-11D, -6.663489723502027E-11D,
			3.625590281552117E-10D, -1.8872497517228294E-9D, 9.381537386495773E-9D, -4.445059128796328E-8D,
			2.0032947535521353E-7D, -8.568720264695455E-7D, 3.4702513081376785E-6D, -1.3273163656039436E-5D,
			4.781565107550054E-5D, -1.6176081582589674E-4D, 5.122859561685758E-4D, -0.0015135724506312532D,
			0.004156422944312888D, -0.010564084894626197D, 0.024726449030626516D, -0.05294598120809499D,
			0.1026436586898471D, -0.17641651835783406D, 0.25258718644363365D};
	protected static final double[] B_i1 = new double[]{7.517296310842105E-18D, 4.414348323071708E-18D,
			-4.6503053684893586E-17D, -3.209525921993424E-17D, 2.96262899764595E-16D, 3.3082023109209285E-16D,
			-1.8803547755107825E-15D, -3.8144030724370075E-15D, 1.0420276984128802E-14D, 4.272440016711951E-14D,
			-2.1015418427726643E-14D, -4.0835511110921974E-13D, -7.198551776245908E-13D, 2.0356285441470896E-12D,
			1.4125807436613782E-11D, 3.2526035830154884E-11D, -1.8974958123505413E-11D, -5.589743462196584E-10D,
			-3.835380385964237E-9D, -2.6314688468895196E-8D, -2.512236237870209E-7D, -3.882564808877691E-6D,
			-1.1058893876262371E-4D, -0.009761097491361469D, 0.7785762350182801D};
	protected static final double[] A_k0 = new double[]{1.374465435613523E-16D, 4.25981614279661E-14D,
			1.0349695257633842E-11D, 1.904516377220209E-9D, 2.5347910790261494E-7D, 2.286212103119452E-5D,
			0.001264615411446926D, 0.0359799365153615D, 0.3442898999246285D, -0.5353273932339028D};
	protected static final double[] B_k0 = new double[]{5.300433772686263E-18D, -1.6475804301524212E-17D,
			5.2103915050390274E-17D, -1.678231096805412E-16D, 5.512055978524319E-16D, -1.848593377343779E-15D,
			6.3400764774050706E-15D, -2.2275133269916698E-14D, 8.032890775363575E-14D, -2.9800969231727303E-13D,
			1.140340588208475E-12D, -4.514597883373944E-12D, 1.8559491149547177E-11D, -7.957489244477107E-11D,
			3.577397281400301E-10D, -1.69753450938906E-9D, 8.574034017414225E-9D, -4.660489897687948E-8D,
			2.766813639445015E-7D, -1.8317555227191195E-6D, 1.39498137188765E-5D, -1.2849549581627802E-4D,
			0.0015698838857300533D, -0.0314481013119645D, 2.4403030820659555D};
	protected static final double[] A_k1 = new double[]{-7.023863479386288E-18D, -2.427449850519366E-15D,
			-6.666901694199329E-13D, -1.4114883926335278E-10D, -2.213387630734726E-8D, -2.4334061415659684E-6D,
			-1.730288957513052E-4D, -0.006975723859639864D, -0.12261118082265715D, -0.3531559607765449D,
			1.5253002273389478D};
	protected static final double[] B_k1 = new double[]{-5.756744483665017E-18D, 1.7940508731475592E-17D,
			-5.689462558442859E-17D, 1.838093544366639E-16D, -6.057047248373319E-16D, 2.038703165624334E-15D,
			-7.019837090418314E-15D, 2.4771544244813043E-14D, -8.976705182324994E-14D, 3.3484196660784293E-13D,
			-1.2891739609510289E-12D, 5.13963967348173E-12D, -2.1299678384275683E-11D, 9.218315187605006E-11D,
			-4.1903547593418965E-10D, 2.015049755197033E-9D, -1.0345762465678097E-8D, 5.7410841254500495E-8D,
			-3.5019606030878126E-7D, 2.406484947837217E-6D, -1.936197974166083E-5D, 1.9521551847135162E-4D,
			-0.002857816859622779D, 0.10392373657681724D, 2.7206261904844427D};

	public static double i0(double var0) throws ArithmeticException {
		if (var0 < 0.0D) {
			var0 = -var0;
		}

		if (var0 <= 8.0D) {
			double var2 = var0 / 2.0D - 2.0D;
			return Math.exp(var0) * Arithmetic.chbevl(var2, A_i0, 30);
		} else {
			return Math.exp(var0) * Arithmetic.chbevl(32.0D / var0 - 2.0D, B_i0, 25) / Math.sqrt(var0);
		}
	}

	public static double i0e(double var0) throws ArithmeticException {
		if (var0 < 0.0D) {
			var0 = -var0;
		}

		if (var0 <= 8.0D) {
			double var2 = var0 / 2.0D - 2.0D;
			return Arithmetic.chbevl(var2, A_i0, 30);
		} else {
			return Arithmetic.chbevl(32.0D / var0 - 2.0D, B_i0, 25) / Math.sqrt(var0);
		}
	}

	public static double i1(double var0) throws ArithmeticException {
		double var4 = Math.abs(var0);
		if (var4 <= 8.0D) {
			double var2 = var4 / 2.0D - 2.0D;
			var4 = Arithmetic.chbevl(var2, A_i1, 29) * var4 * Math.exp(var4);
		} else {
			var4 = Math.exp(var4) * Arithmetic.chbevl(32.0D / var4 - 2.0D, B_i1, 25) / Math.sqrt(var4);
		}

		if (var0 < 0.0D) {
			var4 = -var4;
		}

		return var4;
	}

	public static double i1e(double var0) throws ArithmeticException {
		double var4 = Math.abs(var0);
		if (var4 <= 8.0D) {
			double var2 = var4 / 2.0D - 2.0D;
			var4 = Arithmetic.chbevl(var2, A_i1, 29) * var4;
		} else {
			var4 = Arithmetic.chbevl(32.0D / var4 - 2.0D, B_i1, 25) / Math.sqrt(var4);
		}

		if (var0 < 0.0D) {
			var4 = -var4;
		}

		return var4;
	}

	public static double j0(double var0) throws ArithmeticException {
		double var2;
		double var4;
		double var6;
		double var8;
		if ((var2 = Math.abs(var0)) < 8.0D) {
			var4 = var0 * var0;
			var6 = 5.7568490574E10D + var4 * (-1.3362590354E10D + var4
					* (6.516196407E8D + var4 * (-1.121442418E7D + var4 * (77392.33017D + var4 * -184.9052456D))));
			var8 = 5.7568490411E10D + var4 * (1.029532985E9D
					+ var4 * (9494680.718D + var4 * (59272.64853D + var4 * (267.8532712D + var4 * 1.0D))));
			return var6 / var8;
		} else {
			var4 = 8.0D / var2;
			var6 = var4 * var4;
			var8 = var2 - 0.785398164D;
			double var10 = 1.0D + var6 * (-0.001098628627D
					+ var6 * (2.734510407E-5D + var6 * (-2.073370639E-6D + var6 * 2.093887211E-7D)));
			double var12 = -0.01562499995D + var6
					* (1.430488765E-4D + var6 * (-6.911147651E-6D + var6 * (7.621095161E-7D - var6 * 9.34935152E-8D)));
			return Math.sqrt(0.636619772D / var2) * (Math.cos(var8) * var10 - var4 * Math.sin(var8) * var12);
		}
	}

	public static double j1(double var0) throws ArithmeticException {
		double var2;
		double var4;
		double var6;
		double var8;
		if ((var2 = Math.abs(var0)) < 8.0D) {
			var4 = var0 * var0;
			var6 = var0 * (7.2362614232E10D + var4 * (-7.895059235E9D
					+ var4 * (2.423968531E8D + var4 * (-2972611.439D + var4 * (15704.4826D + var4 * -30.16036606D)))));
			var8 = 1.44725228442E11D + var4 * (2.300535178E9D
					+ var4 * (1.858330474E7D + var4 * (99447.43394D + var4 * (376.9991397D + var4 * 1.0D))));
			return var6 / var8;
		} else {
			double var10 = 8.0D / var2;
			double var12 = var2 - 2.356194491D;
			var4 = var10 * var10;
			var6 = 1.0D + var4
					* (0.00183105D + var4 * (-3.516396496E-5D + var4 * (2.457520174E-6D + var4 * -2.40337019E-7D)));
			var8 = 0.04687499995D + var4
					* (-2.002690873E-4D + var4 * (8.449199096E-6D + var4 * (-8.8228987E-7D + var4 * 1.05787412E-7D)));
			double var14 = Math.sqrt(0.636619772D / var2) * (Math.cos(var12) * var6 - var10 * Math.sin(var12) * var8);
			if (var0 < 0.0D) {
				var14 = -var14;
			}

			return var14;
		}
	}

	public static double jn(int var0, double var1) throws ArithmeticException {
		if (var0 == 0) {
			return j0(var1);
		} else if (var0 == 1) {
			return j1(var1);
		} else {
			double var5 = Math.abs(var1);
			if (var5 == 0.0D) {
				return 0.0D;
			} else {
				double var17;
				int var3;
				double var7;
				double var9;
				double var11;
				double var15;
				if (var5 > (double) var0) {
					var15 = 2.0D / var5;
					var9 = j0(var5);
					var7 = j1(var5);

					for (var3 = 1; var3 < var0; ++var3) {
						var11 = (double) var3 * var15 * var7 - var9;
						var9 = var7;
						var7 = var11;
					}

					var17 = var7;
				} else {
					var15 = 2.0D / var5;
					int var4 = 2 * ((var0 + (int) Math.sqrt(40.0D * (double) var0)) / 2);
					boolean var19 = false;
					double var13 = 0.0D;
					var17 = 0.0D;
					var11 = 0.0D;
					var7 = 1.0D;

					for (var3 = var4; var3 > 0; --var3) {
						var9 = (double) var3 * var15 * var7 - var11;
						var11 = var7;
						var7 = var9;
						if (Math.abs(var9) > 1.0E10D) {
							var7 = var9 * 1.0E-10D;
							var11 *= 1.0E-10D;
							var17 *= 1.0E-10D;
							var13 *= 1.0E-10D;
						}

						if (var19) {
							var13 += var7;
						}

						var19 = !var19;
						if (var3 == var0) {
							var17 = var11;
						}
					}

					var13 = 2.0D * var13 - var7;
					var17 /= var13;
				}

				return var1 < 0.0D && var0 % 2 == 1 ? -var17 : var17;
			}
		}
	}

	public static double k0(double var0) throws ArithmeticException {
		if (var0 <= 0.0D) {
			throw new ArithmeticException();
		} else {
			double var2;
			if (var0 <= 2.0D) {
				var2 = var0 * var0 - 2.0D;
				var2 = Arithmetic.chbevl(var2, A_k0, 10) - Math.log(0.5D * var0) * i0(var0);
				return var2;
			} else {
				double var4 = 8.0D / var0 - 2.0D;
				var2 = Math.exp(-var0) * Arithmetic.chbevl(var4, B_k0, 25) / Math.sqrt(var0);
				return var2;
			}
		}
	}

	public static double k0e(double var0) throws ArithmeticException {
		if (var0 <= 0.0D) {
			throw new ArithmeticException();
		} else {
			double var2;
			if (var0 <= 2.0D) {
				var2 = var0 * var0 - 2.0D;
				var2 = Arithmetic.chbevl(var2, A_k0, 10) - Math.log(0.5D * var0) * i0(var0);
				return var2 * Math.exp(var0);
			} else {
				var2 = Arithmetic.chbevl(8.0D / var0 - 2.0D, B_k0, 25) / Math.sqrt(var0);
				return var2;
			}
		}
	}

	public static double k1(double var0) throws ArithmeticException {
		double var4 = 0.5D * var0;
		if (var4 <= 0.0D) {
			throw new ArithmeticException();
		} else if (var0 <= 2.0D) {
			double var2 = var0 * var0 - 2.0D;
			var2 = Math.log(var4) * i1(var0) + Arithmetic.chbevl(var2, A_k1, 11) / var0;
			return var2;
		} else {
			return Math.exp(-var0) * Arithmetic.chbevl(8.0D / var0 - 2.0D, B_k1, 25) / Math.sqrt(var0);
		}
	}

	public static double k1e(double var0) throws ArithmeticException {
		if (var0 <= 0.0D) {
			throw new ArithmeticException();
		} else if (var0 <= 2.0D) {
			double var2 = var0 * var0 - 2.0D;
			var2 = Math.log(0.5D * var0) * i1(var0) + Arithmetic.chbevl(var2, A_k1, 11) / var0;
			return var2 * Math.exp(var0);
		} else {
			return Arithmetic.chbevl(8.0D / var0 - 2.0D, B_k1, 25) / Math.sqrt(var0);
		}
	}

	public static double kn(int var0, double var1) throws ArithmeticException {
		int var41;
		if (var0 < 0) {
			var41 = -var0;
		} else {
			var41 = var0;
		}

		if (var41 > 31) {
			throw new ArithmeticException("Overflow");
		} else if (var1 <= 0.0D) {
			throw new IllegalArgumentException();
		} else {
			double var32;
			double var8;
			int var40;
			double var12;
			double var18;
			double var20;
			double var22;
			double var24;
			double var26;
			double var28;
			double var30;
			if (var1 <= 9.55D) {
				var26 = 0.0D;
				var22 = 0.25D * var1 * var1;
				var28 = 1.0D;
				var30 = 0.0D;
				double var34 = 1.0D;
				double var38 = 2.0D / var1;
				if (var41 > 0) {
					var30 = -0.5772156649015329D;
					var8 = 1.0D;

					for (var40 = 1; var40 < var41; ++var40) {
						var30 += 1.0D / var8;
						++var8;
						var28 *= var8;
					}

					var34 = var38;
					if (var41 == 1) {
						var26 = 1.0D / var1;
					} else {
						var12 = var28 / (double) var41;
						double var10 = 1.0D;
						var20 = var12;
						var24 = -var22;
						double var16 = 1.0D;

						for (var40 = 1; var40 < var41; ++var40) {
							var12 /= (double) (var41 - var40);
							var10 *= (double) var40;
							var16 *= var24;
							var18 = var12 * var16 / var10;
							var20 += var18;
							if (Double.MAX_VALUE - Math.abs(var18) < Math.abs(var20)) {
								throw new ArithmeticException("Overflow");
							}

							if (var38 > 1.0D && Double.MAX_VALUE / var38 < var34) {
								throw new ArithmeticException("Overflow");
							}

							var34 *= var38;
						}

						var20 *= 0.5D;
						var18 = Math.abs(var20);
						if (var34 > 1.0D && Double.MAX_VALUE / var34 < var18) {
							throw new ArithmeticException("Overflow");
						}

						if (var18 > 1.0D && Double.MAX_VALUE / var18 < var34) {
							throw new ArithmeticException("Overflow");
						}

						var26 = var20 * var34;
					}
				}

				double var36 = 2.0D * Math.log(0.5D * var1);
				var32 = -0.5772156649015329D;
				if (var41 == 0) {
					var30 = var32;
					var18 = 1.0D;
				} else {
					var30 += 1.0D / (double) var41;
					var18 = 1.0D / var28;
				}

				var20 = (var32 + var30 - var36) * var18;
				var8 = 1.0D;

				do {
					var18 *= var22 / (var8 * (var8 + (double) var41));
					var32 += 1.0D / var8;
					var30 += 1.0D / (var8 + (double) var41);
					var20 += (var32 + var30 - var36) * var18;
					++var8;
				} while (Math.abs(var18 / var20) > 1.1102230246251565E-16D);

				var20 = 0.5D * var20 / var34;
				if ((var41 & 1) > 0) {
					var20 = -var20;
				}

				var26 += var20;
				return var26;
			} else if (var1 > 709.782712893384D) {
				throw new ArithmeticException("Underflow");
			} else {
				var8 = (double) var41;
				var30 = 4.0D * var8 * var8;
				var32 = 1.0D;
				var22 = 8.0D * var1;
				var28 = 1.0D;
				var18 = 1.0D;
				var20 = var18;
				double var14 = Double.MAX_VALUE;
				var40 = 0;

				do {
					var24 = var30 - var32 * var32;
					var18 = var18 * var24 / (var28 * var22);
					var12 = Math.abs(var18);
					if (var40 >= var41 && var12 > var14) {
						var26 = Math.exp(-var1) * Math.sqrt(3.141592653589793D / (2.0D * var1)) * var20;
						return var26;
					}

					var14 = var12;
					var20 += var18;
					++var28;
					var32 += 2.0D;
					++var40;
				} while (Math.abs(var18 / var20) > 1.1102230246251565E-16D);

				var26 = Math.exp(-var1) * Math.sqrt(3.141592653589793D / (2.0D * var1)) * var20;
				return var26;
			}
		}
	}

	public static double y0(double var0) throws ArithmeticException {
		double var2;
		double var4;
		double var6;
		if (var0 < 8.0D) {
			var2 = var0 * var0;
			var4 = -2.957821389E9D + var2 * (7.062834065E9D + var2
					* (-5.123598036E8D + var2 * (1.087988129E7D + var2 * (-86327.92757D + var2 * 228.4622733D))));
			var6 = 4.0076544269E10D + var2 * (7.452499648E8D
					+ var2 * (7189466.438D + var2 * (47447.2647D + var2 * (226.1030244D + var2 * 1.0D))));
			return var4 / var6 + 0.636619772D * j0(var0) * Math.log(var0);
		} else {
			var2 = 8.0D / var0;
			var4 = var2 * var2;
			var6 = var0 - 0.785398164D;
			double var8 = 1.0D + var4 * (-0.001098628627D
					+ var4 * (2.734510407E-5D + var4 * (-2.073370639E-6D + var4 * 2.093887211E-7D)));
			double var10 = -0.01562499995D + var4
					* (1.430488765E-4D + var4 * (-6.911147651E-6D + var4 * (7.621095161E-7D + var4 * -9.34945152E-8D)));
			return Math.sqrt(0.636619772D / var0) * (Math.sin(var6) * var8 + var2 * Math.cos(var6) * var10);
		}
	}

	public static double y1(double var0) throws ArithmeticException {
		double var2;
		double var4;
		double var6;
		if (var0 < 8.0D) {
			var2 = var0 * var0;
			var4 = var0 * (-4.900604943E12D + var2 * (1.27527439E12D + var2
					* (-5.153438139E10D + var2 * (7.349264551E8D + var2 * (-4237922.726D + var2 * 8511.937935D)))));
			var6 = 2.49958057E13D + var2 * (4.244419664E11D + var2
					* (3.733650367E9D + var2 * (2.245904002E7D + var2 * (102042.605D + var2 * (354.9632885D + var2)))));
			return var4 / var6 + 0.636619772D * (j1(var0) * Math.log(var0) - 1.0D / var0);
		} else {
			var2 = 8.0D / var0;
			var4 = var2 * var2;
			var6 = var0 - 2.356194491D;
			double var8 = 1.0D + var4
					* (0.00183105D + var4 * (-3.516396496E-5D + var4 * (2.457520174E-6D + var4 * -2.40337019E-7D)));
			double var10 = 0.04687499995D + var4
					* (-2.002690873E-4D + var4 * (8.449199096E-6D + var4 * (-8.8228987E-7D + var4 * 1.05787412E-7D)));
			return Math.sqrt(0.636619772D / var0) * (Math.sin(var6) * var8 + var2 * Math.cos(var6) * var10);
		}
	}

	public static double yn(int var0, double var1) throws ArithmeticException {
		if (var0 == 0) {
			return y0(var1);
		} else if (var0 == 1) {
			return y1(var1);
		} else {
			double var9 = 2.0D / var1;
			double var3 = y1(var1);
			double var5 = y0(var1);

			for (int var11 = 1; var11 < var0; ++var11) {
				double var7 = (double) var11 * var9 * var3 - var5;
				var5 = var3;
				var3 = var7;
			}

			return var3;
		}
	}

	public static double jn(double var0, double var1) throws ArithmeticException {
		if(Math.floor(var0) == var0)
			return jn((int) var0, var1) ;

		IntegralFunction1D func1 = new IntegralFunction1D() {
			@Override
			public double function(double theta) {
				return Math.cos(var1*Math.sin(theta)-var0*theta)/Math.PI ;
			}
		};

		Integral1D result1 = new Integral1D(func1, 0.0, Math.PI) ;

		IntegralFunction1D func2 = new IntegralFunction1D() {
			@Override
			public double function(double t) {
				double x = var1, alpha = var0 ;
				return Math.exp(-x*Math.sinh(t)-alpha*t) ;
			}
		};
		Integral1D result2 = new Integral1D(func2, 0.0, 100.0) ;

		double result = result1.getIntegral() - Math.sin(var0*Math.PI)/Math.PI * result2.getIntegral() ;

		return result ;
	}



}
