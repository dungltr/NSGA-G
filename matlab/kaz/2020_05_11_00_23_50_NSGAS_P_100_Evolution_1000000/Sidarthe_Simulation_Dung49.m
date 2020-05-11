%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% MATLAB Code for epidemic simulations with the SIDARTHE model in the work
% The parameter estimated by NSGA-G
% Modelling the COVID-19 epidemic and implementation of population-wide interventions in Kazakhstan
% the original SIDARTHE code is publish by Giulia Giordano et. al, April 5, 2020
% 
%  
% Contact: trung-dung.le@irisa.fr
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
clear all
close all
clc
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% DATA
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Italian population
popolazione=18e6;

% Data 20 February - 5 April (46 days):
% Total Cases
CasiTotali = totalCase()/popolazione;
% Deaths
Deceduti = totalDeath()/popolazione;
% Recovered
Guariti = totalRecovered()/popolazione;
% Currently Positive
Positivi = CasiTotali - Deceduti - Guariti;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% PARAMETERS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Simulation horizon: CAN BE MODIFIED AT ONE'S WILL PROVIDED THAT IT IS AT
% LEAST EQUAL TO THE NUMBER OF DAYS FOR WHICH DATA ARE AVAILABLE
Orizzonte = 200;

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

R0_iniziale = calculate1(alfa,r1,beta,epsilon,r2,gamma,zeta,r3,delta,eta,r4,theta)

% Time horizon
t=1:step:Orizzonte;
R0NSGA=zeros(1,length(t));
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
    if (i < 8/step)
        [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = setParameterDayAll(1);
    end
    if (i > 8/step)
      if (i < 51/step)
        l = i * step;
        k = ceil (l) - 7;
        [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = setParameterDayAll(k);
      end
    end
    R0NSGA(i)=calculate1(alfa,r1,beta,epsilon,r2,gamma,zeta,r3,delta,eta,r4,theta);
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

figure
plot(t,Infetti_reali,'b',t,I+D+A+R+T,'r',t,H,'g',t,E,'k')
hold on
plot(t,D+R+T+E+H_diagnosticati,'--b',t,D+R+T,'--r',t,H_diagnosticati,'--g')
xlim([t(1) t(end)])
ylim([0 0.015])
axis 'auto y'
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
    %print -djpg PanoramicaEpidemiaRealevsPercepitaF1.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 2 %%%% SuddivisioneInfetti.pdf
figure
plot(t,I,'b',t,D,'c',t,A,'g',t,R,'m',t,T,'r')
xlim([t(1) t(end)])
ylim([0 1.1e-3])
axis 'auto y'
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
    %print -djpg SuddivisioneInfettiF2.jpg
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 3

Figure3 = D+R+T+E+H_diagnosticati;
error3 = 0;
for r = 8:54
    error3 = error3 + (Figure3(r*100) - CasiTotali(r))*(Figure3(r*100) - CasiTotali(r));
end
SQE3 = 1.0273e-07
error3
%%%%%%%%%%%%%
figure
plot(t,D+R+T+E+H_diagnosticati)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),CasiTotali)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
axis 'auto y'
title('Cumulative Diagnosed Cases: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['CasiTotali.pdf'])
    %print -djpg CasiTotaliF3.jpg
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 4
Figure4 = H_diagnosticati;
error4 = 0;
for r = 8:54
    error4 = error4 + (Figure4(r*100) - Guariti(r))*(Figure4(r*100) - Guariti(r));
end
%SQE4 = 5.5446e-08
error4
figure
plot(t,H_diagnosticati)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),Guariti)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
axis 'auto y'
title('Recovered: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the pop%Ơation)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['Guariti_diagnosticati.pdf'])
    %print -djpg Guariti_diagnosticatiF4.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 5
Figure5 = E;
error5 = 0;
for r = 8:54
    error5 = error5 + (Figure5(r*100) - Deceduti(r))*(Figure5(r*100) - Deceduti(r));
end
SQE5 = 4.9117e-07
error5
figure
plot(t,E)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),Deceduti)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
axis 'auto y'
title('Deaths: Model vs. Data - NOTE: EXCLUDED FROM FITTING')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['Morti.pdf'])
    %print -djpg MortiF5.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 6
Figure6 = D+R+T;
error6 = 0;
for r = 8:54
    error6 = error6 + (Figure6(r*100) - Positivi(r))*(Figure6(r*100) - Positivi(r));
end
SQE6 = 3.9855e-07
error6
figure
plot(t,D+R+T)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),Positivi)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
axis 'auto y'
title('Infected: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['Positivi_diagnosticati.pdf'])
    %print -djpg Positivi_diagnosticatiF6.jpg
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 7

figure
plot(t,D)
hold on
%stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Isolamento_domiciliare)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
axis 'auto y'
title('Infected, No Symptoms: estimated by NSGA-G')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['InfettiAsintomatici_diagnosticati.pdf'])
    %print -djpg InfettiAsintomatici_diagnosticatiF7.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 8

figure
plot(t,R)
hold on
%stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Ricoverati_sintomi)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
axis 'auto y'
title('Infected, Symptoms: estimated by NSGA-G')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['InfettiSintomatici_diagnosticati_ricoverati.pdf'])
    %print -djpg InfettiSintomatici_diagnosticati_ricoveratiF8.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 9
figure
plot(t,D+R)
hold on
%stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Isolamento_domiciliare+Ricoverati_sintomi)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
axis 'auto y'
title('Infected, No or Mild Symptoms: estimated by NSGA-G')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['InfettiNonGravi_diagnosticati.pdf'])
    %print -djpg InfettiNonGravi_diagnosticatiF9.jpg
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 10
figure
plot(t,T)
hold on
%stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Terapia_intensiva)
xlim([t(1) t(end)])
ylim([0 2.5e-3])
axis 'auto y'
title('Infected, Life-Threatening Symptoms: estimated by NSGA-G')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['InfettiSintomatici_diagnosticati_terapiaintensiva.pdf'])
    %print -djpg InfettiSintomatici_diagnosticati_terapiaintensivaF10.jpg
end

