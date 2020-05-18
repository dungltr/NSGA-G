function [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = setParameterDayAll(i)
MatrixParameter=[0.3784350672463163,0.2393142107966432,0.0045531267491586425,0.2224886524141444,0.08388150321808467,0.7292184743143447,0.1021163405768431,0.29251479585338314,0.5487629463692846,0.0967749617700651,0.03214091076064725,0.12869431096966,0.0012349377240047491,0.1098560146190957,0.0014605819757646935,0.05793770058176355;0.6707378923179089,0.02312051203880138,0.33475417381107475,8.363409497777605E-4,0.25401574080600614,0.33246773459300893,0.16917305589305284,0.4655830366015062,5.683399642189179E-5,1.7375260482333524E-6,0.0013011579296192315,0.22490275278882632,1.9916505533797862E-4,0.05603093336242241,0.004296811183418402,0.11018041157051175;0.6797336516904506,1.1537742645054262E-4,0.10484437274062207,0.15771416933301366,0.23159724461012712,0.43441408973441903,0.09788069206717794,0.05322191190509271,0.0077008669664153945,0.14485858884212519,1.4760785363680516E-5,0.22378955102842987,0.0060121177675381545,0.10235992588209011,0.003340256453487698,0.1345053613332971;0.7161928819426291,0.0069050725982231255,0.08725493493410043,0.01757837880299523,0.1774478899178808,0.21971807503407312,0.11795908871592037,0.0027021897606810045,0.030964192991459014,0.008562114737039581,0.0564504252714426,0.6271753403853086,0.005353937852990699,0.0039601757482377225,0.006540381893423138,0.005189070573760432;0.7106248620471042,0.3995184045352395,0.12119704128972081,0.1621166151130775,0.613082187866285,0.8458130929178107,0.2102684744329663,0.42835171562458946,0.4633687801874124,0.19270606249362363,0.8954777622801058,0.4952792662819272,0.0170069272871873,0.48151406114733963,0.00150360522354006,0.007601881741690908;0.7421945516292118,0.32428932236537217,0.3725625239417576,0.0010058901053484883,0.6350016856457041,0.7850895499834593,0.4659143312733076,0.13262954356956755,0.09766745756086676,0.008442724136828837,0.016631503968868786,0.12681817735958856,3.1727010583676267E-4,0.13931428176421362,0.011881060854189976,0.005535763683215821;0.9964072779833298,0.05843700344609542,0.3867352815528074,0.162545599465566,0.608013718246877,0.6739473551379841,0.22482059805361343,0.08846689869916163,0.048613677806896995,0.1147754354998735,0.04764290764154744,0.5210972602000924,0.008206818705794584,0.18645445296513796,0.005281523082036158,0.026314787559718972;0.9861525893804839,0.8979718290494536,0.599593578339148,0.0069334675831716165,0.9825748634078467,0.9928030895990896,0.6986808429600364,0.2971672524520224,0.04886564447753104,0.1233746041077586,0.2735972805559823,0.0788974080758407,2.5379865656493714E-4,0.9376981984031456,0.01321597958421792,0.004718436166463551;0.6808416963244767,0.30098436048024096,0.0034784327203128147,0.03459460105543474,0.39062278606260853,0.6122426851490543,0.19123675546587598,0.1154382441516593,0.06539187235927701,0.1299377618341184,0.43185097018084395,0.5253355406425386,0.007622502481127888,0.2191765125566131,0.0016616470425074668,0.05120649189461043;0.8615790527747286,0.009169827830697759,0.27204911300680684,0.004366168301662488,0.3772494892243354,0.9970020043320984,0.7902839016883914,0.14439022372168495,0.016429910486467962,0.10266317670297441,0.05587605965296245,0.023114869388740442,0.0033990749973403264,0.13169483452528108,0.007841479883376409,0.08499582943511676;0.7854681663010136,0.3310892846287965,0.11028459753264047,0.04762930036693801,0.4976998210674759,0.8524882202849917,0.1970326268797145,0.21299213936086986,0.7351984073397405,0.03522969121050694,0.42160068986612864,0.641891462024813,0.0012173938527536435,0.02062903472316195,0.020225024811201975,0.009378819010355477;0.8637087290353345,0.40639020561977296,0.3712502716510838,0.06378496476906177,0.7031318855946855,0.9916319582975394,0.6344463402684923,0.361051289967371,0.849313555339042,0.05581806996301804,0.4318310382787836,0.1428092421273917,0.00819390362527552,0.8442021710617685,0.008713103788755396,0.05753141938958423;0.8777802310497542,0.5370347574927328,0.5889280714783931,0.03381946601447131,0.568947477892481,0.7111916354647665,0.47713381647420644,0.5726473444560058,0.22823216357534953,0.08158699456738303,0.27115322507120043,0.2812662963011664,0.025245229770580687,0.4645718308394019,0.008046845426821497,0.011814317911769411;0.8970383243192183,6.505149712190141E-4,0.6450968628604488,0.058844314432194074,0.7135730417344358,0.8378677758245393,0.5429891200430847,6.602080556876641E-4,0.27150835759311676,0.0312059940641788,0.1291448011040555,0.017437267205931577,0.013438034095367856,0.008307607930634096,2.31340844954936E-4,0.031191219236198123;0.8278669164951873,0.6987043275203435,0.33765338592226407,3.3708864269328204E-4,0.7466171341915844,0.8453287249656498,0.7048862465150704,0.28098399155451875,0.22689799455824455,0.03301862272579679,0.05459313622290929,0.5415028474426119,0.0011082710321379402,0.19808676153257693,0.006611539154017712,0.18262841734886617;0.9057415446642566,0.004460475685932527,0.8640770381890333,0.005904349430348208,0.8878355526336908,0.9665568218891869,0.4347762186873459,0.10951451223566504,0.25901251521530483,0.013442362330996854,0.1469591383024912,0.059094751922184205,0.03325121923405992,0.004430845427091516,2.7244783147196787E-4,0.015084168956006434;0.7556391100607094,0.0029267175771354966,0.021537522748180583,0.15497243843234304,0.3050970360866528,0.9797571177086343,0.6770413382269163,0.2034014114141617,0.2136751608050206,0.07685668983072276,0.3483438546006057,0.5617513335757237,0.042526203676752194,0.15222649180604522,2.591287173949512E-4,0.06116894451569734;0.97195016747917,0.342532454713877,0.5051530202866361,0.261002592824379,0.8271961756804891,0.8542230382172148,0.21421043420317276,0.45985568270084676,0.22978788403420952,0.17795609242469781,0.0313891739893556,0.5662727672818688,0.039624190881370126,0.0756360744879655,0.012045598654170875,0.24909158186909985;0.8293644642993128,0.6849529290295064,0.14795329610570385,0.004001010525147036,0.8513223559843767,0.9319654747638303,0.26309505006590955,0.2623612766090862,0.05780474451733396,0.011736164926770876,0.10102260969132573,0.8013734659428371,0.0036714951541635017,0.09942128551940696,0.008878904020398249,2.476871224218729E-4;0.9739293585353985,0.20112213479764182,0.3679015944381738,0.048945176004349,0.8415948546861889,0.8825589518903378,0.6537007391619967,0.19743961872717203,0.36725343964300805,0.09452733304740372,0.16050711336518622,0.019752328749935395,0.0022356367135061665,0.2804093727448202,0.00982605052763035,0.16674718289242282;0.8975683057713285,9.268570097831569E-5,0.49796986881933936,0.05193094326777947,0.8849846143447722,0.8875963851790579,0.406896429321888,0.0080362716487304,0.2516485773611278,0.03410074985018776,0.28343853365153215,0.0044384386150589554,0.006407464350376355,0.1630099213429768,2.5983641448303584E-4,0.060754252447223836;0.8780000199238855,0.6151459061456377,0.028758623342450468,1.412258717627874E-5,0.8269902075972435,0.8705970329773263,0.8163523466569059,0.1535687201607015,0.22004780069400412,0.03213954973218459,0.1884423177686909,0.5061998752035978,0.018273124293004456,0.41295207614476565,0.0060245588769110795,0.032650237368940425;0.8630177761389412,0.023332728358152525,0.6038932159833892,0.01919588754063631,0.7469834487885542,0.9476191414388535,0.306657901499594,0.01903710706431501,0.2815941557730724,0.005168470332580062,0.21171744092459388,0.4114272902582023,0.005722091280485828,0.11627526885527617,0.011971713386228865,0.011695997706891672;0.8698033126210492,0.18903228370812616,0.012608005923682844,0.0511470802061674,0.5045863032211895,0.7239725791305143,0.47986136201016916,0.08733457447240386,0.6374838987375224,0.03858821824858081,0.08794123345116733,0.3664778492182414,0.05133632819936595,0.23045586409215388,0.0021888815183709375,0.08492010459836928;0.9947269917947842,0.20211972709093945,0.8991283786432327,0.06190269294757034,0.4787463956825,0.69733524799711,0.9967128953919787,0.1942551740938352,0.6738742206446711,0.05712050233593901,0.5242620350128491,0.27276806449252616,0.012946215963325012,0.24335290427704123,0.004962036772104935,0.027105783570780195;0.7264919707075015,0.023214579452465887,0.046019975168219845,0.2417420769981626,0.5149825557323975,0.5945198226348052,0.018063711814309796,0.008683850191984133,0.5932884708123397,0.00197409956941953,0.03492692087625813,0.7622850552258242,0.06075632656463686,0.05217444448417319,0.07501727861787819,0.10593085174998518;0.6770088779038345,0.00705920140878763,0.06670908815249693,0.21698869090318618,0.7780441195096507,0.8515483305891328,0.05877789648495227,0.03287850056331437,0.3603021924088195,0.04920831749300236,0.023527200597823664,0.62844296040637,0.010536425338048466,0.0044637157183516855,0.2080070427567392,7.412893682051592E-4;0.9769689107184081,0.5753110667455039,0.011763986854471054,0.006682777082796673,0.869825181831061,0.8732713727940835,0.7364483507693028,0.44109831616526984,0.5572265203043789,0.07178146700247556,0.32461856256536387,0.7645726875935993,1.777454996344272E-4,0.8663182338074131,0.002354915813840402,0.03862858535078817;0.5184200655546826,0.41611243041664614,0.17470995824865887,9.559376207887021E-5,0.5133567141936417,0.774227868565715,0.8464548360670529,0.2661843120210048,0.8441869438618256,0.07688264329116148,0.24838806390340792,0.14039711998949328,0.0014293779955945726,0.5470511152793269,0.0013642209958399584,0.08394538835258955;0.9539017476633872,0.14667057508611103,0.2176040627573527,0.07319983103084088,0.48817336151280416,0.6776753617595935,0.5932452552438595,0.1901003797579598,0.2238093336303246,0.13593138061095894,0.0337660498431,0.7894447894937289,0.024179258506483055,0.359056857679601,0.020865241273969194,0.16455577146699146;0.5943742776316933,0.001202021577884316,0.018051727623642485,5.501057543036425E-5,0.5473174952653284,0.7381178338138532,0.1623943562152569,0.004563872848960711,0.23738301644519508,0.03886501427313033,0.12631118088129017,0.1427106087627517,2.849525732509517E-4,0.1263185244660528,0.002433379261885322,0.21428641785954672;0.41958862912364897,0.049836912857585666,0.002274413957835661,0.033833930151821054,0.852138047037883,0.9368169968211064,0.6466621201075615,0.09709461155192722,0.12062550615910655,0.03552383133327155,0.009060308560305902,0.0052584597005173005,9.061777979670026E-4,0.5694658696672833,0.04133126978228817,0.01907721247298494;0.6704604698695558,0.5563754412632216,0.032272449531383564,0.08396807003109705,0.8584544339469836,0.8758295765124239,0.9184382928692243,0.3737585521691194,0.7052687473348239,0.2336536416325969,0.2719657129695754,0.6925617195280264,0.03564556284379687,0.3890881167563296,0.001417336547614556,0.12915719017918953;0.5341873830751283,0.42820228257031195,0.03981833547469163,0.04337825093647791,0.7118156574847445,0.7899929482214333,0.7335303035063838,0.3305409905073152,1.278478935334837E-5,0.08421491490549306,0.15706356564204466,0.4000165012239896,0.00946083157222219,0.2934405235017196,0.12022477215917923,0.09071410395960286;0.6786474067542929,0.03196012829792362,7.384463973467668E-5,1.7951802168944368E-4,0.9574218144592368,0.9927845221459052,0.10788226658302433,0.037738045991413414,0.21933311950627454,4.4524154514538616E-4,0.2736590267254723,0.6273472399684813,4.746359764294314E-5,0.1882849349896095,0.0017616069819209785,0.1649015746812721;0.5416609243486579,0.41912986657300727,0.26561198187022356,0.11644480143666991,0.4188665197064965,0.9365321928278458,0.9145717867341479,0.29867940568320095,0.9577399492848705,0.06029811611901067,0.009602540334331888,0.7271858858747428,0.05821054219861463,0.8873284416857588,0.05634206270124026,0.085131907456215;0.3576946160003751,1.2245710842212072E-4,0.2380701272820145,0.20965851759341908,0.07765225169207393,0.20469144734428535,0.030210366376918783,1.435206648442366E-4,0.03640302411859299,6.593392331600764E-5,0.034734841310973895,0.5633529789444198,3.5369228325263864E-4,0.16804925797440715,0.04792872411234338,0.0913557030341313;0.4394799755608949,0.09689364885454421,0.3470470418492626,0.4366351428914597,0.8505335848993258,0.9173587995355486,0.01767466173124993,0.06865991038477573,0.2516479369253786,0.17946729927023247,0.036620422976931605,0.7377293635263777,0.17247125453803278,0.39096462660312403,0.016172560782299544,0.06933869797285268;0.5670559601870379,0.012829867078975543,0.17021674864305544,0.49973597837433437,0.7676908019395399,0.8116571067784486,0.5657540374303055,0.30653420400566456,0.6835490413515799,0.12229428230171166,0.10348318548266325,0.9568101742442942,3.9303252115891385E-4,0.6705584500662447,0.24963961627741849,0.007328551137219154;0.8111713690859989,0.05114701856232816,0.007259979110064735,0.21358900090052962,0.46035169511303276,0.7524288348248763,0.3820748886022014,0.015023744970443884,0.06611979045505226,5.157752830561792E-4,0.11727087218257733,0.9828241222087638,0.06227943249955771,0.6345609562349442,0.15568647389305218,0.4256334230791513;0.6186077046032611,0.33062687444251765,0.002936108918047827,1.0572064616641093E-5,0.5407002847288893,0.5424630232416768,0.10553636775746843,0.3152293640040605,0.06552650000348642,4.337467534982109E-5,0.09002326495275978,0.7782799497199607,5.401416374310947E-6,0.052608264393625044,6.348524875289829E-4,0.030090958255862978;0.7709839200575906,0.030242544273766794,0.047020671987600515,0.01639185509851726,0.5368202863585914,0.6715900431870768,0.42973925350110254,0.00160530749450566,0.24633694108842064,0.00109053616585684,0.31555022897419693,0.6108441257539282,0.03424521525603866,0.6001405101595236,0.032621125366567805,0.18942147890982303;0.48092099766685936,0.20064592634847006,0.05133902939856942,2.549156366308496E-4,0.6607882139055278,0.8217878005174656,0.07990774625433074,0.13887760177974073,0.050841973090445546,0.08510627312994695,0.18250740834485107,0.37918058334360244,0.1393208508839235,0.006872797882156997,8.242553213016294E-5,0.2730597823920253;0.21,0.005,0.11,0.005,0.2,0.3705,0.025,0.025,0.008,0.015,0.01,0.08,0.02,0.02,0.02,0.01];
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