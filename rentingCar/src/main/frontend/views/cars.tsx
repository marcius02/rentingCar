import { CarEndpoint } from 'Frontend/generated/endpoints';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export interface Car {
  id: string;
  make: string;
  model: string;
  year: number;
  color: string;
}

export const config: ViewConfig = {
  menu: { order: 1, icon: 'line-awesome/svg/car-side-solid.svg' },
  title: 'Cars'
};

const sampleCar: Car = {
  id: "123",
  make: "Toyota",
  model: "Camry",
  year: 2024,
  color: "Blue"
};

export default function CarsView() {
  const handleSaveCar = async () => {
    try {
      await CarEndpoint.saveCar(sampleCar);
      alert('Car saved successfully!');
    } catch (error) {
      console.error('Error saving car:', error);
      alert('Failed to save car');
    }
  };

  return (
    <div className="flex flex-col h-full items-center justify-center p-l text-center box-border">
      <img style={{ width: '200px' }} src="images/empty-plant.png" />
      <h2>Car Management</h2>

      <div className="card p-m">
        <pre className="text-left">
          {JSON.stringify(sampleCar, null, 2)}
        </pre>
        <button
          onClick={handleSaveCar}
          className="bg-primary text-white p-s rounded mt-m"
        >
          Save Car
        </button>
      </div>

      <p>It’s a place where you can grow your own UI 🤗</p>
    </div>
  );
}
