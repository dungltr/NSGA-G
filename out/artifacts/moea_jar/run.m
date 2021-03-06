%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% MATLAB Code for epidemic simulations with the SIDARTHE model in the work
%
% Modelling the COVID-19 epidemic and implementation of population-wide interventions in Italy
% by Giulia Giordano, Franco Blanchini, Raffaele Bruno, Patrizio Colaneri, Alessandro Di Filippo, Angela Di Matteo, Marta Colaneri
% 
% Giulia Giordano, April 5, 2020
% Contact: giulia.giordano@unitn.it
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear all
close all
clc

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% DATA
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Italian population
popolazione=60e6;

% Data 20 February - 5 April (46 days):
% Total Cases
CasiTotali = [3 20 79 132 219 322 400 650 888 1128 1694 2036 2502 3089 3858 4636 5883 7375 9172 10149 12462 15113 17660 21157 24747 27980 31506 35713 41035 47021 53578 59138 63927 69176 74386 80539 86498 92472 97689 101739 105792 110574 115242 119827 124632 128948]/popolazione; % D+R+T+E+H_diagnosticati
% Deaths
Deceduti = [0 1 2 2 5 10 12 17 21 29 34 52 79 107 148 197 233 366 463 631 827 1016 1266 1441 1809 2158 2503 2978 3405 4032 4825 5476 6077 6820 7503 8165 9134 10023 10779 11591 12428 13155 13915 14681 15362 15887]/popolazione; % E
% Recovered
Guariti = [0 0 0 1 1 1 3 45 46 50 83 149 160 276 414 523 589 622 724 1004 1045 1258 1439 1966 2335 2749 2941 4025 4440 5129 6072 7024 7432 8326 9362 10361 10950 12384 13030 14620 15729 16847 18278 19758 20996 21815]/popolazione; % H_diagnosticati
% Currently Positive
Positivi = [3 19 77 129 213 311 385 588 821 1049 1577 1835 2263 2706 3296 3916 5061 6387 7985 8514 10590 12839 14955 17750 20603 23073 26062 28710 33190 37860 42681 46638 50418 54030 57521 62013 66414 70065 73880 75528 77635 80572 83049 85388 88274 91246]/popolazione; % D+R+T

% Data 23 February - 5 April (from day 4 to day 46)
% Currently positive: isolated at home
Isolamento_domiciliare = [49 91 162 221 284 412 543 798 927 1000 1065 1155 1060 1843 2180 2936 2599 3724 5036 6201 7860 9268 10197 11108 12090 14935 19185 22116 23783 26522 28697 30920 33648 36653 39533 42588 43752 45420 48134 50456 52579 55270 58320]/popolazione; %D
% Currently positive: hospitalised
Ricoverati_sintomi = [54 99 114 128 248 345 401 639 742 1034 1346 1790 2394 2651 3557 4316 5038 5838 6650 7426 8372 9663 11025 12894 14363 15757 16020 17708 19846 20692 21937 23112 24753 26029 26676 27386 27795 28192 28403 28540 28741 29010 28949]/popolazione; % R
% Currently positive: ICU
Terapia_intensiva = [26 23 35 36 56 64 105 140 166 229 295 351 462 567 650 733 877 1028 1153 1328 1518 1672 1851 2060 2257 2498 2655 2857 3009 3204 3396 3489 3612 3732 3856 3906 3981 4023 4035 4053 4068 3994 3977]/popolazione; %T


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% PARAMETERS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Simulation horizon: CAN BE MODIFIED AT ONE'S WILL PROVIDED THAT IT IS AT
% LEAST EQUAL TO THE NUMBER OF DAYS FOR WHICH DATA ARE AVAILABLE
Orizzonte = 350;

% Plot yes/no: SET TO 1 IF PDF FIGURES MUST BE GENERATED, 0 OTHERWISE
plotPDF = 1;

% Time-step for Euler discretisation of the continuous-time system
step=0.01;
[alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = initParameter()

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% DEFINITIONS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Parameters
[r1,r2,r3,r4,r5] = calParameter(epsilon,zeta,lambda,eta,rho,theta,mu,kappa,nu,xi,sigma,tau);

% Initial R0
%R0_iniziale=alfa/r1+beta*epsilon/(r1*r2)+gamma*zeta/(r1*r3)+delta*eta*epsilon/(r1*r2*r4)+delta*zeta*theta/(r1*r3*r4)
R0_iniziale = calculate1(alfa,r1,beta,epsilon,r2,gamma,zeta,r3,delta,eta,r4,theta)

% Time horizon
%t=1:step:Orizzonte; % old value is Orizzonte
timeLimit = 12;
t=1:step:Orizzonte;

% Vectors for time evolution of variables
S=zeros(1,length(t));
I=zeros(1,length(t));
D=zeros(1,length(t));
A=zeros(1,length(t));
R=zeros(1,length(t));
T=zeros(1,length(t));
H=zeros(1,length(t));
H_diagnosticati=zeros(1,length(t)); % DIAGNOSED recovered only!
E=zeros(1,length(t));

% Vectors for time evolution of actual/perceived Case Fatality Rate
M=zeros(1,length(t));
P=zeros(1,length(t));

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% INITIAL CONDITIONS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

I(1)=200/popolazione;
D(1)=20/popolazione;
A(1)=1/popolazione;
R(1)=2/popolazione;
T(1)=0.00;
H(1)=0.00;
E(1)=0.00;
S(1)=1-I(1)-D(1)-A(1)-R(1)-T(1)-H(1)-E(1);

H_diagnosticati(1) = 0.00; % DIAGNOSED recovered only
Infetti_reali(1)=I(1)+D(1)+A(1)+R(1)+T(1); % Actual currently infected

M(1)=0;
P(1)=0;

% Whole state vector
x=[S(1);I(1);D(1);A(1);R(1);T(1);H(1);E(1);H_diagnosticati(1);Infetti_reali(1)];

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% SIMULATION
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% "Control" binary variables to compute the new R0 every time a policy has
% changed the parameters
plottato = 0;
plottato1 = 0;
plottato_bis = 0;
plottato_tris = 0;
plottato_quat = 0;

for i=2:length(t)
    if (i > 3/step)
      if (i < 4/step)
        [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = setParameterDayAll(2);
      end
    end
    if (i > 4/step)
      if (i < 39/step)
        l = i * step;
        k = fix (l) - 1;
        [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = setParameterDayAll(k);
      end
    end

    if (i>4/step) % Basic social distancing (awareness, schools closed)
        %[alfa, gamma, beta, delta] = Day4()
        if plottato == 0 % Compute the new R0
            [r1,r2,r3,r4,r5] = calParameter(epsilon,zeta,lambda,eta,rho,theta,mu,kappa,nu,xi,sigma,tau);
            R0_primemisure = calculate1(alfa,r1,beta,epsilon,r2,gamma,zeta,r3,delta,eta,r4,theta)
            plottato = 1;
        end
    end
    
    if (i>12/step)
        % Screening limited to / focused on symptomatic subjects
        %epsilon = Day12()
        if plottato1 == 0
            [r1,r2,r3,r4,r5] = calParameter(epsilon,zeta,lambda,eta,rho,theta,mu,kappa,nu,xi,sigma,tau);
            R0_primemisureeps = calculate1(alfa,r1,beta,epsilon,r2,gamma,zeta,r3,delta,eta,r4,theta)
            plottato1 = 1;
        end
      
    end

    if (i>22/step) % Social distancing: lockdown, mild effect
        %[alfa, beta, gamma, delta, mu, nu, zeta, eta, lambda, rho, kappa, xi, sigma] = Day22()   
        if plottato_bis == 0 % Compute the new R0
            [r1,r2,r3,r4,r5] = calParameter(epsilon,zeta,lambda,eta,rho,theta,mu,kappa,nu,xi,sigma,tau);
            R0_secondemisure = calculate2(alfa,r1,beta,epsilon,r2,gamma,zeta,r3,delta,eta,r4,theta)
            plottato_bis = 1;
        end

    end
   
    if (i>28/step) % Social distancing: lockdown, strong effect
        %[alfa, gamma] = Day28()
        if plottato_tris == 0 % Compute the new R0
            [r1,r2,r3,r4,r5] = calParameter(epsilon,zeta,lambda,eta,rho,theta,mu,kappa,nu,xi,sigma,tau);
            R0_terzemisure = calculate2(alfa,r1,beta,epsilon,r2,gamma,zeta,r3,delta,eta,r4,theta)
            plottato_tris = 1;
        end
    end
   
    if (i>38/step) % Broader diagnosis campaign
        %[epsilon, rho, kappa, xi, sigma, zeta, eta] = Day38()
        if plottato_quat == 0 % Compute the new R0
            [r1,r2,r3,r4,r5] = calParameter(epsilon,zeta,lambda,eta,rho,theta,mu,kappa,nu,xi,sigma,tau);
            R0_quartemisure = calculate2(alfa,r1,beta,epsilon,r2,gamma,zeta,r3,delta,eta,r4,theta)
            plottato_quat = 1;
        end
    end
    
    B=[-alfa*x(2)-beta*x(3)-gamma*x(4)-delta*x(5) 0 0 0 0 0 0 0 0 0;
        alfa*x(2)+beta*x(3)+gamma*x(4)+delta*x(5) -(epsilon+zeta+lambda) 0 0 0 0 0 0 0 0;
        0 epsilon  -(eta+rho) 0 0 0 0 0 0 0;
        0 zeta 0 -(theta+mu+kappa) 0 0 0 0 0 0;
        0 0 eta theta -(nu+xi) 0 0 0 0 0;
        0 0 0 mu nu  -(sigma+tau) 0 0 0 0;
        0 lambda rho kappa xi sigma 0 0 0 0;
        0 0 0 0 0 tau 0 0 0 0;
        0 0 rho 0 xi sigma 0 0 0 0;
        alfa*x(2)+beta*x(3)+gamma*x(4)+delta*x(5) 0 0 0 0 0 0 0 0 0];
    
    x=x+B*x*step; % x(n) - x(n-1) = B * x(n-1) *  delta
    
    % Update variables
    
    S(i)=x(1);
    I(i)=x(2);
    D(i)=x(3);
    A(i)=x(4);
    R(i)=x(5);
    T(i)=x(6);
    H(i)=x(7);
    E(i)=x(8);
    
    H_diagnosticati(i)=x(9);
    Infetti_reali(i)=x(10);
    
    % Update Case Fatality Rate
    
    M(i)=E(i)/(S(1)-S(i));
    P(i)=E(i)/((epsilon*r3+(theta+mu)*zeta)*(I(1)+S(1)-I(i)-S(i))/(r1*r3)+(theta+mu)*(A(1)-A(i))/r3);
    
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% FINAL VALUES
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Variables
Sbar=S(length(t));
Ibar=I(length(t));
Dbar=D(length(t));
Abar=A(length(t));
Rbar=R(length(t));
Tbar=T(length(t));
Hbar=H(length(t));
Ebar=E(length(t));

% Case fatality rate
Mbar=M(length(t));
Pbar=P(length(t));

% Case fatality rate from formulas
Mbar1=Ebar/(S(1)-Sbar);
Pbar1=Ebar/((epsilon*r3+(theta+mu)*zeta)*(I(1)+S(1)-Sbar-Ibar)/(r1*r3)+(theta+mu)*(A(1)-Abar)/r3);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% FIGURES 1-10
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 1 %%%% PanoramicaEpidemiaRealevsPercepita.pdf
%{
figure
plot(t,Infetti_reali,'b',t,I+D+A+R+T,'r',t,H,'g',t,E,'k')
hold on
plot(t,D+R+T+E+H_diagnosticati,'--b',t,D+R+T,'--r',t,H_diagnosticati,'--g')
xlim([t(1) t(end)])
ylim([0 0.015])
title('Actual vs. Diagnosed Epidemic Evolution')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
legend({'Cumulative Infected','Current Total Infected', 'Recovered', 'Deaths','Diagnosed Cumulative Infected','Diagnosed Current Total Infected', 'Diagnosed Recovered'},'Location','northwest')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 24 16]);
    set(gcf, 'PaperSize', [24 16]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['PanoramicaEpidemiaRealevsPercepita.pdf'])
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 2 %%%% SuddivisioneInfetti.pdf


figure
plot(t,I,'b',t,D,'c',t,A,'g',t,R,'m',t,T,'r')
xlim([t(1) t(end)])
ylim([0 1.1e-3])
%title('Infected, different stages, Diagnosed vs. Non Diagnosed')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
legend({'Infected ND AS', 'Infected D AS', 'Infected ND S', 'Infected D S', 'Infected D IC'},'Location','northeast')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 24 16]);
    set(gcf, 'PaperSize', [24 16]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['SuddivisioneInfetti.pdf'])
    print -djpg SuddivisioneInfetti.jpg
end
%}
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 3

figure
plot(t,D+R+T+E+H_diagnosticati)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),CasiTotali)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
title('Cumulative Diagnosed Cases: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    %set(gcf, 'PaperUnits', 'centimeters');
    %set(gcf, 'PaperPosition', [0 0 16 10]);
    %set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    %print(gcf,'-dpdf', ['CasiTotali.pdf'])
    print -djpg CasiTotaliF3.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 4

figure
plot(t,H_diagnosticati)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),Guariti)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
title('Recovered: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    %set(gcf, 'PaperUnits', 'centimeters');
    %set(gcf, 'PaperPosition', [0 0 16 10]);
    %set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    %print(gcf,'-dpdf', ['Guariti_diagnosticati.pdf'])
    print -djpg Guariti_diagnosticatiF4.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 5
figure
plot(t,E)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),Deceduti)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
title('Deaths: Model vs. Data - NOTE: EXCLUDED FROM FITTING')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    %set(gcf, 'PaperUnits', 'centimeters');
    %set(gcf, 'PaperPosition', [0 0 16 10]);
    %set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    %print(gcf,'-dpdf', ['Morti.pdf'])
    print -djpg MortiF5.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 6
figure
plot(t,D+R+T)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),Positivi)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
title('Infected: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    %set(gcf, 'PaperUnits', 'centimeters');
    %set(gcf, 'PaperPosition', [0 0 16 10]);
    %set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    %print(gcf,'-dpdf', ['Positivi_diagnosticati.pdf'])
    print -djpg Positivi_diagnosticatiF6.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 7

figure
plot(t,D)
hold on
stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Isolamento_domiciliare)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
title('Infected, No Symptoms: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    %set(gcf, 'PaperUnits', 'centimeters');
    %set(gcf, 'PaperPosition', [0 0 16 10]);
    %set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    %print(gcf,'-dpdf', ['InfettiAsintomatici_diagnosticati.pdf'])
    print -djpg InfettiAsintomatici_diagnosticatiF7.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 8

figure
plot(t,R)
hold on
stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Ricoverati_sintomi)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
title('Infected, Symptoms: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    %set(gcf, 'PaperUnits', 'centimeters');
    %set(gcf, 'PaperPosition', [0 0 16 10]);
    %set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    %print(gcf,'-dpdf', ['InfettiSintomatici_diagnosticati_ricoverati.pdf'])
    print -djpg InfettiSintomatici_diagnosticati_ricoveratiF8.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 9

figure
plot(t,D+R)
hold on
stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Isolamento_domiciliare+Ricoverati_sintomi)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
title('Infected, No or Mild Symptoms: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    %set(gcf, 'PaperUnits', 'centimeters');
    %set(gcf, 'PaperPosition', [0 0 16 10]);
    %set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    %print(gcf,'-dpdf', ['InfettiNonGravi_diagnosticati.pdf'])
    print -djpg InfettiNonGravi_diagnosticatiF9.jpg
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 10

figure
plot(t,T)
hold on
stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Terapia_intensiva)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
title('Infected, Life-Threatening Symptoms: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    %set(gcf, 'PaperUnits', 'centimeters');
    %set(gcf, 'PaperPosition', [0 0 16 10]);
    %set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    %print(gcf,'-dpdf', ['InfettiSintomatici_diagnosticati_terapiaintensiva.pdf'])
    print -djpg InfettiSintomatici_diagnosticati_terapiaintensivaF10.jpg
end
