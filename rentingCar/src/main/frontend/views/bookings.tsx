import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { useEffect, useState } from 'react';
import { UserEndpoint } from 'Frontend/generated/endpoints';
import { Button } from '@vaadin/react-components/Button';

export const config: ViewConfig = {
  menu: { order: 7, icon: 'line-awesome/svg/calendar-solid.svg' },
  title: 'Bookings',
};

export default function Bookings() {
  const [bookings, setBookings] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    UserEndpoint.getBookingsForUser('USER#001')
      .then((result) => setBookings(result ?? []))
      .catch((err) => {
        console.error('Failed to fetch bookings:', err);
        setBookings([]);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading bookings...</div>;
  if (bookings.length === 0) return <div>No bookings found.</div>;

  return (
    <div>
      <h2>Bookings for USER#001</h2>
      <ul>
        {bookings.map((b, i) => (
          <li key={i}>
            {b.car?.make} {b.car?.model} ({b.startDate} → {b.endDate}) - {b.status}
          </li>
        ))}
      </ul>
    </div>
  );
}
