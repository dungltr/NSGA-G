%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% MATLAB Code for epidemic simulations with the SIDARTHE model in the work
% The parameter estimated by NSGA-G
% Modelling the COVID-19 epidemic and implementation of population-wide interventions in Kazakhstan
% the original SIDARTHE code is published by Giulia Giordano et. al, April 5, 2020
% 
%  
% Contact: trung-dung.le@irisa.fr
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = setParameterDayAll(i)
MatrixParameter=[0.45842115176255355,0.038780002134314255,0.1303550121219824,0.7392723449558701,0.06425868685262753,0.6204578569151948,0.13473582850229954,0.23277053636212044,0.0889957401715623,0.29150350403438985,0.039142032142112385,0.11101264411066632,0.009691531493745303,0.10442401669469446,0.005628849627131962,0.0245026304719993;0.22591192443814456,0.12178212154566262,0.06554084707974514,0.001426969972050853,0.21375892812548797,0.22975342095598936,0.027771821567728058,0.48374371709226144,1.1850521527670542E-4,0.0017976185168217852,4.678607639950971E-4,0.16150117642795955,9.310714208469527E-4,0.07657055981070504,5.623077353909248E-5,0.12118310954389835;0.4140484295212193,2.30804635116292E-5,0.11703896607088564,0.23847794861412852,0.22477848156228364,0.34380940458858117,0.18079877959750557,3.448610919178632E-5,0.12698390607232896,0.04585217229879115,6.869256173716172E-7,0.09807631225899985,0.03641162610390864,0.10146271322307335,0.001577709662615094,0.008263576538667678;0.28449532314369724,0.03825307062338928,0.008431647100162079,0.017801384887815446,0.18886999097175197,0.13961426676168504,0.0034417032759359012,0.042199513655754396,0.028926946105477424,0.02253055662520146,0.05632983228229747,0.020154547099968705,1.732677226346975E-5,0.02771691925108844,0.0032475855389450165,0.04510101757156117;0.732341873129828,0.8634194371090728,0.30169540565017583,0.32836002054523167,0.5241202406450954,0.4546673950873998,0.03712660125867925,0.5997838838027475,0.5920014569536736,0.24797370248790332,0.8949531824200472,0.6895575326128893,1.7256164359963996E-5,0.5530239118518381,1.3250288597804605E-4,0.10586594734579745;0.3778124777873012,0.05818128333809964,0.09792989424135491,0.06899949907088365,0.5727829388178217,0.3442368779258027,0.09352852361076304,0.2690654415631179,0.10947652139023635,0.02592073338352902,0.016778500642795045,0.11181822679321937,9.538407227575518E-5,0.05347783285334942,0.006583611282295122,0.03422602973931618;0.19079711296931545,0.20516147210848787,0.023909725602491688,0.19920829858367292,0.7053744817645655,0.18643957783704268,0.017140204888452578,0.21054230989914655,0.008481901159269169,0.13904679059762592,0.04756783084662652,0.027027968397286464,0.0012832207099404292,0.07359087107030698,0.0012382070903155646,0.08239639138873756;0.8105971984144299,0.8353665878930843,0.7537639044628324,0.014939458433449185,0.9901158056892064,0.9811430150608988,0.4309140307936907,0.3221401649241585,0.40101733899793296,0.08523222817264904,0.2770767522512552,0.04215553973554049,6.697017691112188E-4,0.3703931616451421,1.6826677073482127E-4,0.037569245971315404;0.3074751803495946,0.28615685205366714,0.03566161140924801,0.24042568713678517,0.47171009186138657,0.6306261818515554,0.07781632378614906,0.1719413409824787,0.03379637758615559,0.1314608495670026,0.43194524124565215,0.5358879614932468,0.004163313555413948,0.2233895111444993,0.017997246582874174,0.012793789109588817;0.020107730909850647,0.7137223868503589,0.032039571606149134,0.09529156078606936,0.6230096447226147,0.15453366592819906,0.001855244732948581,0.3848199238859916,0.18396292105873166,0.07832086197274417,0.05600892472724095,0.09004965020314544,0.010076388328609139,0.5721229052134267,0.014276593602712993,0.024170174286839832;0.1792918844290926,0.30153997343542366,0.172094324722641,0.2558307874963026,0.6106934664572671,0.14170004060303415,0.1261369261631947,0.44710584967371386,0.641318615576918,0.1224102424958927,0.4492384018422794,0.059107992221194054,0.012189598973343057,0.10894913084382854,0.010090202209710512,0.013350416535513962;0.7937098448880042,0.7313284513478516,0.15628708928164498,9.449856878126372E-4,0.5480392636540968,0.3387639888730917,0.6776331128694639,0.4307439746652668,0.3296297808091591,0.07617006236753186,0.4430215011343319,0.19146468856928076,7.265566968759815E-4,0.33298357322151867,0.01009888132236008,0.07864170658827081;0.22018008080777302,0.1288458563216609,0.6450501789730372,0.23717255997989087,0.4017691031829798,0.4946540089305718,0.5770977604175924,0.49191171576999365,0.09083290975621872,0.08693918148159212,0.27014776318466915,0.0979401478607872,0.007425874360531083,0.3871553571641595,0.013778440109266687,0.029637841763865296;0.7387742569919048,0.1371337350789782,0.018703234685683676,0.10580325813257258,0.9317800225748294,0.38496868120571104,0.17699186026290895,0.15803737254597564,0.07643814412233742,0.06197758216432927,0.1280684874492206,0.16744113595454374,0.0011520721752157675,0.3008983512461666,0.004304695022565244,0.04509790144003286;0.6344285461324684,0.4977814230164301,0.8451381195223675,0.1006413369128275,0.9509854746852135,0.5607351093844501,0.46814161418487676,0.4157591500878314,0.1071094563183465,0.022592370627798803,0.054459844618831396,0.24865752294006147,0.01234129925533803,0.4215677835844712,0.03443378693997273,0.003843611917621545;0.3464868336246837,0.3702305260623895,0.8760055118331644,0.10157151804105116,0.8631369534459647,0.875311169660828,0.24433572922032767,0.26298413846026497,0.04642277515753195,0.06070698461110564,0.14791357188078047,0.020734867776198363,1.1424488314180335E-5,0.4611300885388499,0.0062488109583469734,0.10500541279328299;0.19448611711324043,0.08146973976833577,0.3880355918484576,0.23060861477358127,0.3674635988059665,0.46340839238590703,0.17051859636895592,0.3703862708041493,0.0025930716599343482,0.10126218232916068,0.3486334194875022,0.08987421324179612,0.037012568742265155,0.0355914475911953,0.0032191744518327414,0.06404315598202173;0.7870221742288747,0.3027847331310219,0.02975742675328259,0.04811746587270218,0.9989010823476148,0.5179507336293141,0.04131635591476525,0.270843022911413,0.6134090700247828,0.03451898512735333,0.04225075395857202,0.6789181237922723,0.0017640207701418158,0.52614062972632,0.0053391245074934784,0.1662755984116438;0.9550577574104412,0.4171052652841831,0.024244160005940905,0.08046931372473147,0.9963303811668656,0.6495520699521539,0.3841327738925505,0.21910845513161994,0.5561710181528345,0.025262680989711545,0.19833446947783195,0.1464417309625398,3.5560415743711245E-5,0.35522428162337444,0.03035199526463432,6.036901175605436E-4;0.9931989095894431,0.7611324752329215,0.6731365397529808,0.17946224817090223,0.5428791208242226,0.28157941604280406,0.1300182881632173,0.2035557756896024,0.032947333082716274,0.06419728867216319,0.14609778299074552,0.9025165125086018,0.02112352633810725,0.8705738623352036,0.003410626136242819,0.08883500913701575;0.4716564528857297,0.2400699520308134,0.23120166232131747,0.08004432084060104,0.6085112495809362,0.1752983345385227,0.9472286006603874,0.14937763709159435,0.105587823556986,0.06050370264848953,0.377568806408564,0.013424585493413375,0.010462268267205569,0.10247498981253043,0.01040970910279626,0.013205787562521151;0.14508408702740314,0.44630393730744244,0.1496159066176347,0.211702556320216,0.5526235648440844,0.41340388626345853,0.07328068551317353,0.09652222901932005,0.03470221397296825,0.05009393509888371,0.23138808270318525,0.8369119547308278,0.008261457041129894,0.31152315415622034,0.029379189027563923,0.019129736983451;0.6922700667834275,0.4708359051150021,0.4365533663762401,0.08040867022916606,0.3068660099959983,0.4361335742627068,0.36086218848394525,0.09762089142854752,0.13053445156119758,0.03837826310117826,0.23582947897849554,0.38246583340182005,0.01326023207865191,0.3063686378965503,0.012985531408468564,0.050001171111753416;0.08461205489140902,0.06148963502923727,0.23846741040595415,0.09203238953803809,0.31725407137759093,0.38517595766409884,0.06634957837542967,0.14407763972376017,0.07171042561936511,0.0377628335175113,0.09823735333219942,0.12185705746965383,0.005267360693906347,0.0028254998341796108,0.006518806126813842,0.10781627536883424;0.7656963529291693,0.4394711712252054,0.015256560454996577,0.006332661940758059,0.3247007234249366,0.22725950821499175,0.11670810553678589,0.17475057675022665,0.15893314592594915,0.07267246131049895,0.5031089052701329,0.1918321460370319,0.038825107501516264,0.12986140701155793,5.9085357229293714E-5,0.014042100812179726;0.04877230790226929,0.17248142636331362,0.12600398112486866,0.12257334310507488,0.3763440838864757,0.23358230286858794,0.4466549258108729,0.0763371317150196,0.09635784763884421,0.022675279228416084,0.17626501184256865,0.1896317326468472,0.008850272785125086,0.11394182926231924,0.015974062267992684,0.028264193253639845;0.5770596380398287,0.10352749252169186,0.12165655493682574,0.013966841875162455,0.645905867115332,0.03394082293124479,0.08708274473249482,0.07351224533436247,0.04842437947681977,0.0686398672287861,0.2672137584902727,0.09048331017414829,0.002625317745432916,5.8813742296279356E-5,0.005134474832455776,0.1941962744298486;0.5584282123624805,0.08015625451191274,0.05825976034103136,0.1101270157251987,0.39992529495277257,0.5388104877874736,0.028629468123362627,0.027961818229053352,0.1376074044225883,0.04323348157007675,0.337975094835195,0.2569726308589286,0.025693972450825088,0.22348619624274413,0.009539670046445035,0.0368075099440445;0.5855176043335774,0.24406647793906902,0.19172935136963054,0.06127392796979843,0.2866337104936138,0.6078593945094802,0.05581330729479691,0.10527058960051217,0.04062396251968911,0.07455963715694397,0.32524816338117973,0.20694519867185956,0.01057983225596798,0.1934612368255488,0.001725709951977262,0.12888910568619213;0.36049749692901245,0.33006670199912724,0.2544681552950654,0.0034369790215092446,0.1957244579656694,0.1486033769758763,0.01167197206858442,0.03847900559766701,0.4991221295554691,0.0146652091407036,0.1301421938664894,0.4179424925268999,0.012305139268930326,0.1803114739639671,6.655471639157723E-5,0.092696815626607;0.18498979770965587,0.1802689613029732,0.09806125268895219,0.06316017223150104,0.22358493695708548,0.18903524557336876,0.258554029287568,0.08039977826785236,0.07639133335552535,0.059009401585118595,0.27276781614988277,0.22478084679509516,0.002384069125211487,0.2258038251530416,0.01336294652337028,0.11998697922812997;0.26994703931753167,0.26597148916227115,0.34544384956495466,1.4080814382673234E-4,0.2272424392926239,0.04822074592327683,0.059119233902251364,0.0811020738146589,0.0687628910214128,0.03731592147405296,0.3013962500358436,0.2420832671414459,2.3436647692471382E-4,0.01113772264156333,0.028025393747666504,0.013148338313161407;0.10545910454784864,0.02218137086095257,0.1823754300407113,0.1165536607057233,0.18652893424390166,0.3958955825025895,0.3562634022048944,0.004191725859284833,0.16318474869819546,0.008940756193476534,0.28177521663243604,0.656614189000831,5.7617202758784894E-5,0.2651120894448409,0.025462235332474145,0.01448046534611992;0.243205267983162,0.014870180881631593,0.4442199671319938,0.02589804884551547,0.5451328695320927,0.07981071731629787,0.018648730406809113,0.05955243157974302,0.058708304236447975,0.017437437586968068,0.1601572919824537,0.17657038187059237,0.004181082134628458,0.15320221224596037,0.031013459175760795,0.009351698014429225;0.43479454754668995,0.05237949144739403,0.21052607830676967,0.027950415327693344,0.4212400435159999,0.26222351133076705,0.10099007692125518,0.016009538966602153,0.22802521919471408,0.03423786498579016,0.3755481410317395,0.11924894923364242,0.016108430279460625,0.08402451974194874,0.0022904120120210624,0.056581232402668435;0.004199704219192926,0.13673025861732624,0.18121113197408617,0.0071064667693388605,0.4994533622909623,0.09214675946878753,0.094093123914974,0.03516096219679085,0.07149105386260798,0.0374799844490061,0.1664521057763969,0.286765591363595,0.004548785705406249,0.14653946385873165,7.875005733707688E-4,0.14112473614268997;0.1991551849851146,0.045765154838717154,0.22802010266932515,0.13193062517188034,0.32584248968445123,0.03306075593551126,0.24159802127177168,0.03745377330503157,0.25581287584534174,0.03148187616134264,0.40444002965775777,0.16364930549056073,0.006248961841480572,0.1831966067767045,0.016405646901282996,0.02287681263212515;0.7142820499234079,0.07036448315919826,0.17946338249351565,0.0892970134143806,0.17084853021036073,0.3648654373366336,0.186688380513335,0.009618770793609168,0.1691561038633285,0.02511413016287419,0.276806606076344,0.42454623986637696,0.0016451463431330087,0.22633646051698544,0.021968627162410465,0.03710931664571384;0.30277289978638816,0.09897664135906432,0.12108123064840294,0.06970102369779067,0.2715369063472179,0.0033994881245625035,0.007230267265320408,0.03786262255916861,0.0963110461449445,0.0421766947294259,0.27665263178621535,0.2878277909702753,8.260251540142868E-4,0.5459976495048302,0.013198745439295572,0.07794207287244019;0.36589510165236216,0.38664556053754856,0.7700378397580006,0.49651604884949274,0.16042542175137497,0.1618089524515299,0.0,0.04136154266675425,0.03751171969496508,0.07334062987718686,0.35370367186740415,0.8379345731557244,1.8067521632643397E-6,0.18592862594784126,0.0021311296450540014,0.17266957463980798;0.1584993208620663,0.07665920026390016,0.0033907127397969626,0.012749063010833334,0.10901169835544998,0.10456449972908695,0.030595744058293784,0.027520041020433636,0.23203962827363275,0.04186183867752669,0.3655249397471014,0.006958304815814282,0.00995644715963841,0.018032035227770235,0.00638153900715382,0.017660162241420658;0.20257428511244463,0.01868422179845753,0.11927660902533954,0.003183586828196764,0.12231159784104469,1.3401970093267606E-4,0.07575670814599467,0.03328335512638289,0.05623812061576085,0.035043103697757344,0.3080789496992312,0.128051827908551,0.0027475520080502414,0.16885476257799847,0.017858485959646776,0.0042866123550920135;0.5322877920458433,1.2231834951161258E-4,0.05883918959759255,0.01291996765511463,0.09064175145230234,0.06874343904244261,6.041905179922478E-4,0.010775762365370622,0.11505195036658215,0.015021301109969498,0.20475152557500975,0.24067899436907833,4.538041619584541E-5,0.10961407206846274,0.016648024037594807,0.009588896567569406;0.21,0.005,0.11,0.005,0.2,0.3705,0.025,0.025,0.008,0.015,0.01,0.08,0.02,0.02,0.02,0.01];
alfa=MatrixParameter(i,1);
beta=MatrixParameter(i,2);
gamma=MatrixParameter(i,3);
delta=MatrixParameter(i,4);
epsilon=MatrixParameter(i,5);
theta=MatrixParameter(i,6);
zeta=MatrixParameter(i,7);
eta=MatrixParameter(i,8);
mu=MatrixParameter(i,9);
nu=MatrixParameter(i,10);
tau=MatrixParameter(i,11);
lambda=MatrixParameter(i,12);
rho=MatrixParameter(i,13);
kappa=MatrixParameter(i,14);
xi=MatrixParameter(i,15);
sigma=MatrixParameter(i,16);
end