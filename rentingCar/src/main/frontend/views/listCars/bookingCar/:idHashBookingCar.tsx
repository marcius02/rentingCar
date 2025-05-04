import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { useEffect, useState } from 'react';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
//import { useNavigate, useParams, useLocation } from '@vaadin/hilla-file-router/react';
import { DelegationEndpoint, UserEndpoint } from 'Frontend/generated/endpoints';
import { DatePicker } from '@vaadin/react-components/DatePicker';
import { Select } from '@vaadin/react-components/Select';
import { Button } from '@vaadin/react-components/Button';

export const config: ViewConfig = {
  menu: { exclude: true },
  title: 'Complete Booking'
};

export default function BookingCar() {
  const { idHashBookingCar } = useParams<{ idHashBookingCar: string }>();
  const navigate = useNavigate();
  const location = useLocation();

  const car = location.state?.car;

  const [delegations, setDelegations] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    startDate: '',
    endDate: '',
    pickupDelegationId: undefined as any,
    deliverDelegationId: undefined as any
  });

  useEffect(() => {
    const loadDelegations = async () => {
      try {
        const result = await DelegationEndpoint.getAllProfileDelegations();
        setDelegations(result || []);
      } catch (error) {
        console.error('Error loading delegations:', error);
      } finally {
        setLoading(false);
      }
    };
    loadDelegations();
  }, []);

  const handleSubmit = async () => {
    if (!formData.startDate || !formData.endDate) {
      alert('Please select start and end dates');
      return;
    }
    if (!car) {
      alert('Car data is missing.');
      return;
    }
    if (!formData.pickupDelegationId || !formData.deliverDelegationId) {
      alert('Please select pickup and delivery delegations');
      return;
    }

    try {
      await UserEndpoint.saveBooking({
        userId: "USER#001",
        operation: 'booking#2025#005',
        car: car,
        startDate: formData.startDate,
        endDate: formData.endDate,
        pickUpDelegation: formData.pickupDelegationId,
        deliverDelegation: formData.deliverDelegationId,
        status: 'PENDING',
        totalToPayment: 0,
        statusPayment: "PENDING",
        statusBooking: "CREATED"
      });
      navigate('/bookings');
    } catch (error) {
      console.error('Booking failed:', error);
      alert('Failed to complete booking');
    }
  };

  if (!car) {
    return <div>Error: Car data not found. Please navigate from the car list.</div>;
  }

  if (loading) return <div>Loading...</div>;

  return (
    <div className="p-m max-w-2xl mx-auto">
      <h2 className="text-xl mb-l">Booking ID: {idHashBookingCar}</h2>
      <div className="space-y-m">
      <div className="space-y-m">
        <DatePicker
          label="Start Date"
          required
          min={new Date().toISOString().split('T')[0]}
          onValueChanged={(e) => setFormData({...formData, startDate: e.detail.value})}
        />
       </div>
        <div className="space-y-m">
        <DatePicker
          label="End Date"
          required
          min={formData.startDate}
          onValueChanged={(e) => setFormData({...formData, endDate: e.detail.value})}
        />
         </div>
        <Select
          label="Pickup Location"
          value={formData.pickupDelegationId}
          items={delegations.map(d => ({ label: d.name, value: d }))}
          onValueChanged={e => setFormData({ ...formData, pickupDelegationId: e.detail.value })}
        />
         <div className="space-y-m">
        <Select
          label="Return Location"
          value={formData.deliverDelegationId}
          items={delegations.map(d => ({ label: d.name, value: d }))}
          onValueChanged={e => setFormData({ ...formData, deliverDelegationId: e.detail.value })}
        />
         </div>
        <div className="mt-xl">
          <Button theme="primary" onClick={handleSubmit}>
            Confirm Booking
          </Button>
        </div>
      </div>
    </div>
  );
}
