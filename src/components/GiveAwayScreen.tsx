import React, { useState } from 'react';
import { ArrowLeft, CheckCircle2, PackageCheck, Sparkles } from 'lucide-react';

interface Props {
  onNavigateBack: () => void;
  onSubmitSuccess: () => void;
}

export const GiveAwayScreen: React.FC<Props> = ({
  onNavigateBack,
  onSubmitSuccess,
}) => {
  const [itemTitle, setItemTitle] = useState('');
  const [category, setCategory] = useState('Furniture');
  const [address, setAddress] = useState('42 Oak St, Apt 4B');
  const [isSubmitted, setIsSubmitted] = useState(false);

  return (
    <div className="flex flex-col h-full bg-[#F8FAFC] text-slate-800 p-4 overflow-y-auto">
      <div className="flex items-center space-x-2 mb-4">
        <button
          onClick={onNavigateBack}
          className="w-8 h-8 rounded-full bg-white border border-slate-200 flex items-center justify-center cursor-pointer hover:bg-slate-50 transition-colors"
        >
          <ArrowLeft className="w-4 h-4 text-slate-700" />
        </button>
        <h1 className="text-sm font-bold text-slate-900">
          Give Away Item
        </h1>
      </div>

      {isSubmitted ? (
        <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm text-center my-auto">
          <div className="w-14 h-14 bg-emerald-50 text-emerald-600 rounded-2xl flex items-center justify-center mx-auto mb-3">
            <CheckCircle2 className="w-8 h-8" />
          </div>
          <h2 className="text-lg font-bold mb-1 text-slate-900">
            Pickup Scheduled
          </h2>
          <p className="text-xs text-slate-500 mb-5 leading-relaxed">
            Municipal eco-van will collect your items on Tuesday morning. You've earned <strong className="text-emerald-600 font-semibold">+150 Eco Points</strong>!
          </p>
          <button
            onClick={onSubmitSuccess}
            className="w-full py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl font-medium text-xs shadow-sm transition-colors cursor-pointer"
          >
            Return to Dashboard
          </button>
        </div>
      ) : (
        <div className="space-y-4">
          <div className="bg-indigo-50/70 border border-indigo-100 rounded-xl p-3 text-xs text-indigo-700 flex items-start gap-2">
            <Sparkles className="w-4 h-4 text-indigo-600 mt-0.5 shrink-0" />
            <span>Pass along reusable items before disposal. Divert landfill waste & earn community points.</span>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1.5">
              1. Item Name
            </label>
            <input
              type="text"
              value={itemTitle}
              onChange={(e) => setItemTitle(e.target.value)}
              placeholder="e.g. Wooden Dining Chair, Working Microwave"
              className="w-full p-2.5 bg-white border border-slate-200 rounded-xl text-xs font-medium text-slate-900 focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition-all"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1.5">
              2. Category
            </label>
            <div className="grid grid-cols-2 gap-2">
              {['Furniture', 'Electronics', 'Textiles', 'Tools'].map((cat) => (
                <button
                  key={cat}
                  onClick={() => setCategory(cat)}
                  className={`p-2.5 rounded-xl border text-xs font-medium transition-all ${
                    category === cat
                      ? 'bg-indigo-600 text-white border-indigo-600 shadow-sm'
                      : 'bg-white text-slate-700 border-slate-200 hover:border-slate-300'
                  }`}
                >
                  {cat}
                </button>
              ))}
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1.5">
              3. Pickup Address
            </label>
            <input
              type="text"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              className="w-full p-2.5 bg-white border border-slate-200 rounded-xl text-xs font-medium text-slate-900 focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition-all"
            />
          </div>

          <button
            onClick={() => setIsSubmitted(true)}
            className="w-full mt-2 py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-medium text-xs rounded-xl shadow-sm shadow-indigo-100 transition-all cursor-pointer"
          >
            Submit Giveaway Request
          </button>
        </div>
      )}
    </div>
  );
};
