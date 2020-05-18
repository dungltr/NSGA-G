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

% Data (54 days): 13/03/2020 until 05/05/2020
% Total Cases
CasiTotali = totalCase()/popolazione;  % D+R+T+E+H_diagnosticati
% Deaths
Deceduti = totalDeath()/popolazione;  % E
% Recovered
Guariti = totalRecovered()/popolazione; % H_diagnosticati
% Currently Positive
Positivi = CasiTotali - Deceduti - Guariti; % D+R+T

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% PARAMETERS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Simulation horizon: CAN BE MODIFIED AT ONE'S WILL PROVIDED THAT IT IS AT
% LEAST EQUAL TO THE NUMBER OF DAYS FOR WHICH DATA ARE AVAILABLE
Orizzonte = 200;

% Plot yes/no: SET TO 1 IF PDF FIGURES MUST BE GENERATED, 0 OTHERWISE
plotPDF = 0;

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
[startDate stopDate] = setDate();

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% "Control" binary variables to compute the new R0 every time a policy has
% changed the parameters
plottato = 0;
plottato1 = 0;
plottato_bis = 0;
plottato_tris = 0;
plottato_quat = 0;

for i=2:length(t)
    if (i < startDate/step)
        [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = setParameterDayAll(1);
    end
    if (i > startDate/step)
      if (i < (stopDate-2)/step)
        l = i * step;
        k = ceil (l) - startDate + 1;
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
Figure12
Figure310
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


