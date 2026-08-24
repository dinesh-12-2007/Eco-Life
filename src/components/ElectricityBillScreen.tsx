import React, { useState } from 'react';
import { User, ElectricityProvider, PointsConversionConfig, BillPaymentReceipt, ElectricityBill } from '../types';
import { ArrowLeft, Zap, CheckCircle, AlertCircle, Building2, CreditCard, ShieldCheck, History } from 'lucide-react';

interface Props {
  user: User;
  providers: ElectricityProvider[];
  conversionConfig: PointsConversionConfig;
  paymentHistory: BillPaymentReceipt[];
  onFetchBill: (providerId: string, consumerNumber: string) => ElectricityBill;
  onPayBill: (
    providerId: string,
    consumerNumber: string,
    billNumber: string,
    totalAmount: number,
    pointsToRedeem: number,
    onResult: (res: { success: boolean; receipt?: BillPaymentReceipt; error?: string }) => void
  ) => void;
  onNavigateBack: () => void;
}

export const ElectricityBillScreen: React.FC<Props> = ({
  user,
  providers,
  conversionConfig,
  paymentHistory,
  onFetchBill,
  onPayBill,
  onNavigateBack,
}) => {
  const [selectedProviderId, setSelectedProviderId] = useState(providers[0]?.id || 'prov-01');
  const [consumerNumber, setConsumerNumber] = useState('90283471');
  const [fetchedBill, setFetchedBill] = useState<ElectricityBill | null>(null);
  const [pointsToRedeem, setPointsToRedeem] = useState<number>(0);
  const [isProcessing, setIsProcessing] = useState(false);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [receipt, setReceipt] = useState<BillPaymentReceipt | null>(null);
  const [activeTab, setActiveTab] = useState<'PAY' | 'HISTORY'>('PAY');

  const selectedProvider = providers.find((p) => p.id === selectedProviderId) || providers[0];

  const handleFetch = (e: React.FormEvent) => {
    e.preventDefault();
    if (!consumerNumber.trim()) {
      setErrorMsg('Please enter a valid consumer connection number.');
      return;
    }
    setErrorMsg(null);
    setReceipt(null);
    const bill = onFetchBill(selectedProviderId, consumerNumber.trim());
    setFetchedBill(bill);

    // Default points to redeem = min(balance, points needed for full bill)
    const maxPointsForBill = Math.floor(bill.billAmount * conversionConfig.pointsPerUnit);
    setPointsToRedeem(Math.min(user.balancePoints, maxPointsForBill));
  };

  const handlePayment = () => {
    if (!fetchedBill) return;
    setIsProcessing(true);
    setErrorMsg(null);

    onPayBill(
      selectedProviderId,
      fetchedBill.consumerNumber,
      fetchedBill.billNumber,
      fetchedBill.billAmount,
      pointsToRedeem,
      (res) => {
        setIsProcessing(false);
        if (res.success && res.receipt) {
          setReceipt(res.receipt);
          setFetchedBill(null);
        } else {
          setErrorMsg(res.error || 'Payment failed. Please try again.');
        }
      }
    );
  };

  const discountAmount = fetchedBill ? pointsToRedeem / conversionConfig.pointsPerUnit : 0;
  const netPayable = fetchedBill ? Math.max(0, fetchedBill.billAmount - discountAmount) : 0;
  const maxRedeemablePoints = fetchedBill
    ? Math.min(user.balancePoints, Math.floor(fetchedBill.billAmount * conversionConfig.pointsPerUnit))
    : 0;

  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 overflow-y-auto">
      {/* Header */}
      <div className="bg-white border-b border-slate-200 px-4 py-3 flex items-center justify-between sticky top-0 z-10">
        <div className="flex items-center space-x-3">
          <button
            onClick={onNavigateBack}
            className="w-8 h-8 rounded-full bg-slate-100 hover:bg-slate-200 flex items-center justify-center text-slate-600 transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h1 className="font-bold text-sm text-slate-900 leading-none">
              Electricity Bill Payment
            </h1>
            <p className="text-[10px] text-slate-500 mt-0.5">
              Redeem reward points for municipal utility credits
            </p>
          </div>
        </div>
        <div className="w-8 h-8 rounded-lg bg-amber-500/10 text-amber-600 flex items-center justify-center">
          <Zap className="w-4 h-4 fill-current" />
        </div>
      </div>

      <div className="p-4 space-y-4">
        {/* Segmented Control */}
        <div className="flex rounded-xl bg-slate-200/70 p-1">
          <button
            onClick={() => setActiveTab('PAY')}
            className={`flex-1 py-1.5 text-xs font-bold rounded-lg transition-all ${
              activeTab === 'PAY' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Pay Electricity Bill
          </button>
          <button
            onClick={() => setActiveTab('HISTORY')}
            className={`flex-1 py-1.5 text-xs font-bold rounded-lg transition-all flex items-center justify-center gap-1.5 ${
              activeTab === 'HISTORY' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            <History className="w-3.5 h-3.5" />
            <span>History ({paymentHistory.length})</span>
          </button>
        </div>

        {activeTab === 'HISTORY' ? (
          <div className="space-y-3">
            {paymentHistory.length === 0 ? (
              <div className="bg-white border border-slate-200 rounded-2xl p-8 text-center text-slate-400 text-xs">
                No electricity bill payment records yet.
              </div>
            ) : (
              paymentHistory.map((h) => (
                <div key={h.paymentId} className="bg-white border border-slate-200 rounded-2xl p-4 shadow-xs space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="bg-emerald-50 text-emerald-700 text-[10px] font-bold px-2 py-0.5 rounded-full">
                      {h.status}
                    </span>
                    <span className="text-[10px] text-slate-400 font-mono">{h.timestamp}</span>
                  </div>
                  <div>
                    <h3 className="text-xs font-bold text-slate-900">{h.providerName}</h3>
                    <p className="text-[11px] text-slate-500">
                      Consumer #{h.consumerNumber} • Bill #{h.billNumber}
                    </p>
                  </div>
                  <div className="pt-2 border-t border-slate-100 flex items-center justify-between text-xs">
                    <div>
                      <span className="text-[10px] text-slate-400 block">Points Redeemed</span>
                      <span className="font-bold text-emerald-600">
                        -{h.pointsRedeemed} PTS ({conversionConfig.currencySymbol}{h.pointsDiscountAmount.toFixed(2)})
                      </span>
                    </div>
                    <div className="text-right">
                      <span className="text-[10px] text-slate-400 block">Net Paid</span>
                      <span className="font-extrabold text-slate-900">
                        {conversionConfig.currencySymbol}{h.amountPaid.toFixed(2)}
                      </span>
                    </div>
                  </div>
                  <div className="text-[9px] text-slate-400 font-mono truncate">Ref: {h.transactionRef}</div>
                </div>
              ))
            )}
          </div>
        ) : (
          <>
            {/* Wallet Balance Card */}
            <div className="bg-gradient-to-r from-amber-500 to-amber-600 rounded-2xl p-4 text-slate-950 shadow-sm relative overflow-hidden">
              <div className="flex items-center justify-between">
                <div>
                  <div className="text-[10px] font-bold uppercase tracking-wider text-amber-950/80">
                    Your Reward Wallet
                  </div>
                  <div className="text-2xl font-black">{user.balancePoints.toLocaleString()} PTS</div>
                  <div className="text-xs font-bold text-amber-950">
                    ≈ {conversionConfig.currencySymbol}{(user.balancePoints / conversionConfig.pointsPerUnit).toFixed(2)} Credit
                  </div>
                </div>
                <div className="w-10 h-10 rounded-xl bg-amber-950/10 flex items-center justify-center text-amber-950">
                  <CreditCard className="w-5 h-5" />
                </div>
              </div>
              <div className="mt-2 pt-2 border-t border-amber-950/10 text-[11px] font-medium text-amber-950 flex items-center gap-1">
                <Zap className="w-3 h-3 fill-current" />
                <span>Conversion rate: 100 points = {conversionConfig.currencySymbol}10 bill discount</span>
              </div>
            </div>

            {/* Step 1: Provider & Consumer Form */}
            <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm space-y-3">
              <h2 className="text-xs font-bold uppercase tracking-wider text-slate-500 flex items-center gap-1.5">
                <Building2 className="w-3.5 h-3.5 text-indigo-600" />
                <span>1. Select Electricity Provider & Account</span>
              </h2>

              <form onSubmit={handleFetch} className="space-y-3">
                <div>
                  <label className="block text-[11px] font-bold text-slate-700 mb-1">
                    Electricity Board / Distribution Company
                  </label>
                  <select
                    value={selectedProviderId}
                    onChange={(e) => {
                      setSelectedProviderId(e.target.value);
                      setFetchedBill(null);
                    }}
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl px-3 py-2 text-xs font-medium text-slate-900 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  >
                    {providers.map((p) => (
                      <option key={p.id} value={p.id}>
                        {p.name} ({p.state})
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-[11px] font-bold text-slate-700 mb-1">
                    Consumer / Connection Account Number
                  </label>
                  <input
                    type="text"
                    value={consumerNumber}
                    onChange={(e) => {
                      setConsumerNumber(e.target.value);
                      setFetchedBill(null);
                    }}
                    placeholder="e.g. 90283471"
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl px-3 py-2 text-xs font-mono font-medium text-slate-900 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>

                <button
                  type="submit"
                  className="w-full bg-slate-900 hover:bg-slate-800 text-white font-bold py-2.5 rounded-xl text-xs transition-colors cursor-pointer"
                >
                  Fetch Current Bill
                </button>
              </form>
            </div>

            {errorMsg && (
              <div className="bg-rose-50 border border-rose-200 rounded-xl p-3 text-xs text-rose-700 flex items-start gap-2">
                <AlertCircle className="w-4 h-4 shrink-0 mt-0.5" />
                <span>{errorMsg}</span>
              </div>
            )}

            {/* Step 2 & 3: Bill details & Points slider */}
            {fetchedBill && (
              <div className="space-y-4">
                {/* Bill Card */}
                <div className="bg-white border border-slate-200 rounded-2xl p-4 shadow-sm space-y-3">
                  <div className="flex items-center justify-between">
                    <h3 className="text-xs font-bold uppercase tracking-wider text-slate-500">
                      2. Current Bill Details
                    </h3>
                    <span className="bg-amber-50 text-amber-700 text-[10px] font-bold px-2 py-0.5 rounded-full">
                      {fetchedBill.status}
                    </span>
                  </div>

                  <div className="grid grid-cols-2 gap-2 text-xs">
                    <div>
                      <span className="text-[10px] text-slate-400 block">Consumer</span>
                      <span className="font-semibold text-slate-900">{fetchedBill.consumerName}</span>
                    </div>
                    <div className="text-right">
                      <span className="text-[10px] text-slate-400 block">Bill Number</span>
                      <span className="font-semibold text-slate-900">{fetchedBill.billNumber}</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-slate-400 block">Billing Period</span>
                      <span className="font-medium text-slate-700">{fetchedBill.billingMonth}</span>
                    </div>
                    <div className="text-right">
                      <span className="text-[10px] text-slate-400 block">Due Date</span>
                      <span className="font-bold text-rose-600">{fetchedBill.dueDate}</span>
                    </div>
                  </div>

                  <div className="pt-2 border-t border-slate-100 flex items-center justify-between">
                    <span className="text-xs font-bold text-slate-700">Total Bill Amount:</span>
                    <span className="text-lg font-extrabold text-slate-900">
                      {conversionConfig.currencySymbol}{fetchedBill.billAmount.toFixed(2)}
                    </span>
                  </div>
                </div>

                {/* Points Redemption Slider Card */}
                <div className="bg-gradient-to-br from-indigo-50 to-emerald-50 border border-indigo-100 rounded-2xl p-4 shadow-sm space-y-3">
                  <div className="flex items-center justify-between">
                    <h3 className="text-xs font-bold uppercase tracking-wider text-indigo-900">
                      3. Apply Reward Points Credit
                    </h3>
                    <span className="text-xs font-extrabold text-emerald-700">{pointsToRedeem} PTS</span>
                  </div>

                  <p className="text-[11px] text-slate-600">
                    Use slider to adjust points discount (100 pts = {conversionConfig.currencySymbol}10 credit):
                  </p>

                  <input
                    type="range"
                    min="0"
                    max={maxRedeemablePoints}
                    step="10"
                    value={pointsToRedeem}
                    onChange={(e) => setPointsToRedeem(Number(e.target.value))}
                    className="w-full h-2 bg-indigo-200 rounded-lg appearance-none cursor-pointer accent-indigo-600"
                  />

                  <div className="flex gap-2">
                    <button
                      type="button"
                      onClick={() => setPointsToRedeem(0)}
                      className="flex-1 py-1 px-2 text-[11px] font-bold bg-white border border-slate-200 rounded-lg text-slate-700 hover:bg-slate-50"
                    >
                      Use None (0)
                    </button>
                    <button
                      type="button"
                      onClick={() => setPointsToRedeem(maxRedeemablePoints)}
                      className="flex-1 py-1 px-2 text-[11px] font-bold bg-emerald-600 text-white rounded-lg hover:bg-emerald-700"
                    >
                      Use Max ({maxRedeemablePoints} pts)
                    </button>
                  </div>

                  {/* Pricing Breakdown */}
                  <div className="pt-2 border-t border-indigo-100/80 space-y-1 text-xs">
                    <div className="flex justify-between text-slate-600">
                      <span>Original Bill:</span>
                      <span>{conversionConfig.currencySymbol}{fetchedBill.billAmount.toFixed(2)}</span>
                    </div>
                    <div className="flex justify-between text-emerald-700 font-semibold">
                      <span>Points Discount (-{pointsToRedeem} pts):</span>
                      <span>-{conversionConfig.currencySymbol}{discountAmount.toFixed(2)}</span>
                    </div>
                    <div className="flex justify-between text-sm font-extrabold text-slate-900 pt-1 border-t border-indigo-100">
                      <span>Net Payable Amount:</span>
                      <span>{conversionConfig.currencySymbol}{netPayable.toFixed(2)}</span>
                    </div>
                  </div>
                </div>

                {/* Confirm & Pay Button */}
                <button
                  onClick={handlePayment}
                  disabled={isProcessing}
                  className="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-black py-3 rounded-2xl text-xs transition-colors cursor-pointer shadow-md shadow-emerald-200 flex items-center justify-center gap-2"
                >
                  <ShieldCheck className="w-4 h-4" />
                  <span>{isProcessing ? 'Processing Settlement...' : 'Confirm & Pay Electricity Bill'}</span>
                </button>
              </div>
            )}

            {/* Receipt Modal/Card */}
            {receipt && (
              <div className="bg-emerald-50 border-2 border-emerald-300 rounded-2xl p-5 shadow-sm space-y-3">
                <div className="flex items-center space-x-2 text-emerald-800">
                  <CheckCircle className="w-5 h-5 fill-emerald-600 text-white" />
                  <h3 className="font-extrabold text-sm">Payment Successful & Recorded!</h3>
                </div>
                <p className="text-xs text-emerald-900">
                  Your utility credit was applied successfully. Transaction reference has been logged.
                </p>
                <div className="bg-white/80 rounded-xl p-3 text-xs space-y-1.5 font-mono text-slate-800">
                  <div className="flex justify-between">
                    <span className="text-slate-500 font-sans">Payment Ref:</span>
                    <span className="font-bold">{receipt.transactionRef}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-slate-500 font-sans">Provider:</span>
                    <span>{receipt.providerName}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-slate-500 font-sans">Consumer #:</span>
                    <span>{receipt.consumerNumber}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-slate-500 font-sans">Points Redeemed:</span>
                    <span className="font-bold text-emerald-600">
                      -{receipt.pointsRedeemed} PTS ({conversionConfig.currencySymbol}{receipt.pointsDiscountAmount.toFixed(2)})
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-slate-500 font-sans">Net Paid:</span>
                    <span className="font-bold">{conversionConfig.currencySymbol}{receipt.amountPaid.toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-slate-500 font-sans">New Balance:</span>
                    <span className="font-bold text-indigo-600">{receipt.updatedWalletBalance} PTS</span>
                  </div>
                </div>
                <button
                  onClick={() => setReceipt(null)}
                  className="w-full bg-slate-900 text-white font-bold py-2 rounded-xl text-xs hover:bg-slate-800 transition-colors"
                >
                  Pay Another Bill
                </button>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};
